package com.ruoyi.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.dto.PageTestObjectDto;
import com.ruoyi.basic.dto.TestItemDto;
import com.ruoyi.basic.mapper.*;
import com.ruoyi.basic.pojo.*;
import com.ruoyi.basic.service.CapacityScopeService;
import com.ruoyi.basic.service.StandardProductListService;
import com.ruoyi.basic.service.StructureItemParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检验项目参数(StructureItemParameter)表服务实现类
 *
 * @author makejava
 * @since 2024-02-26 16:21:17
 */
@Service
@AllArgsConstructor
public class CapacityScopeServiceImpl extends ServiceImpl<StructureItemParameterMapper, StructureItemParameter> implements CapacityScopeService {

    private StructureItemParameterMapper structureItemParameterMapper;

    private StructureTestObjectMapper structureTestObjectMapper;

    private ProductMapper productMapper;

    private StructureTestObjectPartMapper structureTestObjectPartMapper;

    private ProductPartMapper productPartMapper;

    private StandardProductListService standardProductListService;

    private StandardTreeMapper standardTreeMapper;

    private StructureItemParameterService structureItemParameterService;

    @Override
    public IPage<StructureItemParameter> selectItemParameterList(Page page, StructureItemParameter itemParameter) {
       return structureItemParameterMapper.selectItemParameterList(page, QueryWrappers.queryWrappers(itemParameter));
    }

    @Override
    public int addItemParameter(StructureItemParameter itemParameter) {
        if (itemParameter.getBsm().equals("")||itemParameter.getBsm()==null){
            itemParameter.setBsm("0");
        }
        int insert = structureItemParameterMapper.insert(itemParameter);
        return insert;
    }

    @Override
    public int delItemParameter(Integer id) {
        return structureItemParameterMapper.deleteById(id);
    }

    @Override
    public int upItemParameter(StructureItemParameter itemParameter) {
        return structureItemParameterMapper.updateById(itemParameter);
    }

    @Override
    public IPage<PageTestObjectDto> selectTestObjectList(Page page, PageTestObjectDto pageTestObjectDto) {
        String partNo = pageTestObjectDto.getPartNo();
        pageTestObjectDto.setPartNo(null);
        return structureTestObjectMapper.selectTestObjectList(page, QueryWrappers.queryWrappers(pageTestObjectDto),partNo);
    }

    @Override
    public int addTestObject(StructureTestObject testObject) {
        Long count = structureTestObjectMapper.selectCount(Wrappers.<StructureTestObject>lambdaQuery().eq(StructureTestObject::getSpecimenName, testObject.getSpecimenName()));
        if(count.compareTo(0L) > 0){
            throw new BaseException("检验对象不能重复");
        }

        return structureTestObjectMapper.insert(testObject);
    }

    @Override
    public int delTestObject(Integer id) {
        // 产出检验对象产品维护
        structureTestObjectPartMapper.delete(Wrappers.<StructureTestObjectPart>lambdaQuery()
                .eq(StructureTestObjectPart::getTestObjectId, id));

        // 删除产品维护的零件绑定
        List<Product> products = productMapper.selectList(Wrappers.<Product>lambdaQuery()
                .eq(Product::getObjectId, id));
        List<Integer> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(productIds)) {
            productPartMapper.delete(Wrappers.<ProductPart>lambdaQuery()
                    .in(ProductPart::getProductId, productIds));

            // 删除产品维护
            productMapper.delete(Wrappers.<Product>lambdaQuery()
                    .in(Product::getId, productIds));
        }

        return structureTestObjectMapper.deleteById(id);
    }

    @Override
    public int upTestObject(StructureTestObject testObject) {
        // 查询旧的检验对象
        StructureTestObject oldTestObject = structureTestObjectMapper.selectById(testObject.getId());

        if (!oldTestObject.getSpecimenName().equals(testObject.getSpecimenName())) {
            // 查询所有对象一样的检验项目
            List<StandardProductList> standardProductLists = standardProductListService.list(Wrappers.<StandardProductList>lambdaUpdate()
                    .eq(StandardProductList::getSampleType, oldTestObject.getSpecimenName()));
            if (CollectionUtils.isNotEmpty(standardProductLists)){
                for (StandardProductList standardProductList : standardProductLists) {
                    // 修改所有的对象名称和数型结构
                    standardProductList.setSampleType(testObject.getSpecimenName());
                    // 需要截取第三级, 避免三四级名称一样修改错误
                    String[] trees = standardProductList.getTree().split(" - ");
                    trees[2] = testObject.getSpecimenName();
                    List<String> list = CollUtil.newArrayList(trees);
                    String newName = CollUtil.join(list, " - ");
                    standardProductList.setTree(newName);
                }
                standardProductListService.updateBatchById(standardProductLists);
            }
            // 修改检验项目参数的检验对象
            // 拼接 ["object", 查询检验项目参数修改绑定的检验对象
            String format = "[\"{}\",";
            String sampleOld = StrUtil.format(format, oldTestObject.getSpecimenName());
            List<StructureItemParameter> itemParameterList = structureItemParameterService.list(Wrappers.<StructureItemParameter>lambdaQuery()
                    .like(StructureItemParameter::getSample, sampleOld));
            if (CollectionUtils.isNotEmpty(itemParameterList)) {
                for (StructureItemParameter structureItemParameter : itemParameterList) {
                    // 修改绑定的样品名称
                    String sampleNew = StrUtil.format(format, testObject.getSpecimenName());
                    String sampleUp = structureItemParameter.getSample().replace(sampleOld, sampleNew);
                    structureItemParameter.setSample(sampleUp);
                }
                structureItemParameterService.updateBatchById(itemParameterList);
            }

            // 修改树的型号
            standardTreeMapper.update(null, Wrappers.<StandardTree>lambdaUpdate()
                    .eq(StandardTree::getSampleType, oldTestObject.getSpecimenName())
                    .set(StandardTree::getSampleType, testObject.getSpecimenName()));
        }

        Long count = structureTestObjectMapper.selectCount(Wrappers.<StructureTestObject>lambdaQuery()
                .eq(StructureTestObject::getSpecimenName, testObject.getSpecimenName())
                .ne(StructureTestObject::getId, testObject.getId()));
        if(count.compareTo(0L) > 0){
            throw new BaseException("检验对象不能重复");
        }

        return structureTestObjectMapper.updateById(testObject);
    }

    @Override
    public List<StructureTestObject> selectTestObjectByName() {
        return structureTestObjectMapper.selectList(Wrappers.<StructureTestObject>lambdaQuery().select(StructureTestObject::getSpecimenName,StructureTestObject::getId));
    }

    //设备里面选择检验项目(树形结构)
    @Override
    public List<Map<String, Object>> getInsProduction() {
        return structureItemParameterMapper.getInsProduction();
    }

    @Override
    public List<TestItemDto> getItemTree() {
        return structureItemParameterMapper.getItemTree();
    }
}

