package com.ruoyi.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.dto.ProductDTO1;
import com.ruoyi.basic.excel.StructureTestObjectData;
import com.ruoyi.basic.mapper.ProductMapper;
import com.ruoyi.basic.mapper.ProductPartMapper;
import com.ruoyi.basic.mapper.StandardTreeMapper;
import com.ruoyi.basic.mapper.StructureTestObjectMapper;
import com.ruoyi.basic.pojo.*;
import com.ruoyi.basic.service.LaboratoryService;
import com.ruoyi.basic.service.ProductService;
import com.ruoyi.basic.service.StandardProductListService;
import com.ruoyi.basic.service.StructureItemParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author z1292
* @description 针对表【product(产品表)】的数据库操作Service实现
* @createDate 2024-04-26 01:11:02
*/
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

    private ProductMapper productMapper;

    private LaboratoryService laboratoryService;

    private StructureTestObjectMapper structureTestObjectMapper;

    private ProductPartMapper productPartMapper;

    private StandardProductListService standardProductListService;

    private StandardTreeMapper standardTreeMapper;

    private StructureItemParameterService structureItemParameterService;

    @Override
    public IPage<Product> selectProductListByObjectId(Page page, ProductDTO1 product) {
        String partNo = product.getPartNo();
        product.setPartNo(null);
        return productMapper.selectProductListByObjectId(page, QueryWrappers.queryWrappers(product), partNo);
    }

    @Override
    public int addProduct(Product product) {

        return productMapper.insert(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upProduct(Product product) {
        // 查询原本的名称
        Product oldProduct = productMapper.selectById(product.getId());

        if (!oldProduct.getName().equals(product.getName())) {
            // 修改名称匹配的标准树下的检验项目
            // 查询所有对象+名称的树
            StructureTestObject testObject = structureTestObjectMapper.selectById(oldProduct.getObjectId());

            List<StandardProductList> standardProductLists = standardProductListService.list(Wrappers.<StandardProductList>lambdaUpdate()
                    .eq(StandardProductList::getSample, oldProduct.getName())
                    .eq(StandardProductList::getSampleType, testObject.getSpecimenName()));
            if (CollectionUtils.isNotEmpty(standardProductLists)) {
                for (StandardProductList standardProductList : standardProductLists) {
                    // 修改样品名称
                    standardProductList.setSample(product.getName());
                    // 修改树名称
                    // 需要截取第四级, 避免三四级名称一样修改错误
                    String[] trees = standardProductList.getTree().split(" - ");
                    trees[3] = product.getName();
                    List<String> list = CollUtil.newArrayList(trees);
                    String newName = CollUtil.join(list, " - ");
                    standardProductList.setTree(newName);
                }
                standardProductListService.updateBatchById(standardProductLists);
            }

            // 修改检验项目参数的检验对象
            // 拼接["object","product"]查询检验项目参数修改绑定的检验对象
            String format = "[\"{}\",\"{}\"]";
            String sampleOld = StrUtil.format(format, testObject.getSpecimenName(), oldProduct.getName());
            List<StructureItemParameter> itemParameterList = structureItemParameterService.list(Wrappers.<StructureItemParameter>lambdaQuery()
                    .like(StructureItemParameter::getSample, sampleOld));
            if (CollectionUtils.isNotEmpty(itemParameterList)) {
                for (StructureItemParameter structureItemParameter : itemParameterList) {
                    // 修改绑定的样品名称
                    String sampleNew = StrUtil.format(format, testObject.getSpecimenName(), product.getName());
                    String sampleUp = structureItemParameter.getSample().replace(sampleOld, sampleNew);
                    structureItemParameter.setSample(sampleUp);
                }
                structureItemParameterService.updateBatchById(itemParameterList);
            }

            // 修改树的型号
            standardTreeMapper.update(null, Wrappers.<StandardTree>lambdaUpdate()
                    .eq(StandardTree::getSampleType, testObject.getSpecimenName())
                    .eq(StandardTree::getSample, oldProduct.getName())
                    .set(StandardTree::getSample, product.getName()));
        }

        return productMapper.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delProduct(Integer id) {
        // 删除零件绑定
        productPartMapper.delete(Wrappers.<ProductPart>lambdaQuery()
                .eq(ProductPart::getProductId, id));

        return productMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importPartExcel(List<StructureTestObjectData> list) {
        list.forEach(i -> {
            // 检验对象
            StructureTestObject structureTestObject1 = structureTestObjectMapper.selectOne(Wrappers.<StructureTestObject>lambdaQuery()
                    .eq(StructureTestObject::getSpecimenName, i.getSpecimenName())
                    .eq(StructureTestObject::getSpecimenNameEn, i.getSpecimenNameEn()));
            Laboratory laboratory = laboratoryService.getOne(Wrappers.<Laboratory>lambdaQuery()
                    .eq(Laboratory::getLaboratoryName, i.getLaboratory()));
            if (ObjectUtils.isEmpty(laboratory)) {
                throw new BaseException("未找到该场所：" + i.getLaboratory() + "，请检查是否存在该场所！");
            }
            // 如果为空进行新增
            if(ObjectUtils.isEmpty(structureTestObject1)) {
                StructureTestObject structureTestObject = new StructureTestObject();
                structureTestObject.setLaboratoryId(laboratory.getId());
                structureTestObject.setSpecimenName(i.getSpecimenName());
                structureTestObject.setSpecimenNameEn(i.getSpecimenNameEn());
                structureTestObject.setCode(i.getCode());
                structureTestObjectMapper.insert(structureTestObject);

                // 产品
                Product product = productMapper.selectOne(Wrappers.<Product>lambdaQuery()
                        .eq(Product::getName, i.getName())
                        .eq(Product::getNameEn, i.getNameEn()));
                if (ObjectUtils.isEmpty(product)){
                    Product product1 = new Product();
                    product1.setName(i.getName());
                    product1.setNameEn(i.getNameEn());
                    product1.setObjectId(structureTestObject.getId());
                    baseMapper.insert(product1);
                }
            } else {
                structureTestObject1.setCode(i.getCode());
                structureTestObject1.setLaboratoryId(laboratory.getId());
                structureTestObjectMapper.updateById(structureTestObject1);
                // 产品
                Product product = productMapper.selectOne(Wrappers.<Product>lambdaQuery()
                        .eq(Product::getName, i.getName())
                        .eq(Product::getNameEn, i.getNameEn()));
                if (ObjectUtils.isEmpty(product)){
                    Product product1 = new Product();
                    product1.setName(i.getName());
                    product1.setNameEn(i.getNameEn());
                    product1.setObjectId(structureTestObject1.getId());
                    baseMapper.insert(product1);
                } else {
                    product.setName(i.getName());
                    product.setNameEn(i.getNameEn());
                    product.setObjectId(structureTestObject1.getId());
                    baseMapper.updateById(product);
                }
            }
        });
    }
}
