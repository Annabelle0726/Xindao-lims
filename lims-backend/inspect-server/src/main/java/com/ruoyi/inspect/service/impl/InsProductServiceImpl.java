package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.StandardProductListMapper;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.InsProductBindingDto;
import com.ruoyi.inspect.mapper.InsOrderMapper;
import com.ruoyi.inspect.mapper.InsProductMapper;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.service.InsProductService;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InsProductServiceImpl extends ServiceImpl<InsProductMapper, InsProduct>
        implements InsProductService {

    private InsProductMapper insProductMapper;

    private InsOrderMapper insOrderMapper;

    private InsSampleMapper insSampleMapper;

    private StandardProductListMapper standardProductListMapper;

    @Override
    public int selectOrderManDay(Integer orderId) {
        return insProductMapper.selectOrderManDay(orderId);
    }

    @Override
    public int updateInspected(Integer id, String ids) {
        InsOrder insOrder = new InsOrder();
        insOrder.setId(id);
        insOrder.setIsRevocation(1);//需要审核
        insOrder.setRevocationInsProductIds(ids);
        return insOrderMapper.updateById(insOrder);
    }

    @Override
    public boolean write(InsOrder insOrder) {
        insOrderMapper.updateById(insOrder);
        return true;
    }

    //查询待检项目
    @Override
    public IPage<InsProduct> selectNoProducts(Page page, InsProduct insProduct, Integer orderId, String ids) {
        List<Integer> noIds = null;
        if (StringUtils.isNotBlank(ids)) {
            noIds = Arrays.asList(ids.split(",")).stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        IPage<InsProduct> insProductIPage = baseMapper.selectNoProducts(page, orderId, noIds);
        return insProductIPage;
    }

    //审核待检撤销
    @Override
    public void checkUpdate(Integer orderId, Integer state) {
        InsOrder insOrder = insOrderMapper.selectById(orderId);
        if (state == 1) {
            List<String> list = new ArrayList<>();
            try {
                list = Arrays.asList(insOrder.getRevocationInsProductIds().split(","));
            } catch (Exception e) {
                throw new ErrorException("还没有选择应该要撤销的检验项目");
            }
            List<Integer> ids = list.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, orderId));
            List<Integer> collect = insSamples.stream().map(InsSample::getId).collect(Collectors.toList());
            List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery().eq(InsProduct::getState, 1).in(InsProduct::getInsSampleId, collect));
            if (insProducts.stream().map(InsProduct::getId).collect(Collectors.toList()).equals(ids)) {
                //如果该检验单下的所有项目全部都撤销,那么这个单子也需要撤销
                insOrder.setState(3);
            } else {
                List<InsProduct> products = insProductMapper.selectBatchIds(ids);
                //首先根据选择的项目id进行撤销项目
                updateBatchById(products.stream().map(insProduct -> {
                    insProduct.setState(0);
                    return insProduct;
                }).collect(Collectors.toList()));
            }
            //insProductMapper.updateInspected(id);
        }
        //不通过
        insOrder.setIsRevocation(0);
        insOrderMapper.updateById(insOrder);

    }


    /**
     * 根据检验项id查询检验项树信息
     * @param productId
     * @return
     */
    @Override
    public List<InsProduct> getProductTreeByProductId(Integer productId) {
        // 查询检验项信息
        InsProduct insProduct = insProductMapper.selectById(productId);
        String tree = insProduct.getFactory() + " - " +
                insProduct.getLaboratory() + " - " +
                insProduct.getSampleType() + " - " +
                insProduct.getSample() + " - " +
                insProduct.getModel();
        // 查询标准树
        List<StandardProductList> standardProductLists = standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                .eq(StandardProductList::getStandardMethodListId, insProduct.getStandardMethodListId())
                .eq(StandardProductList::getTree, tree));

        List<InsProduct> insProducts = standardProductLists.stream().map(standardProductList -> {
            InsProduct product = new InsProduct();
            BeanUtils.copyProperties(standardProductList, product);
            // 防止序列化错误
            product.setTemplate(null);
            product.setStyle(null);
            return product;
        }).sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort())
                - (o2.getSort() == null ? 0 : o2.getSort())).collect(Collectors.toList());
        return insProducts;
    }

    /**
     * 特殊检验项绑定
     * @param insProductBindingDto
     * @return
     */
    @Override
    public boolean bindingProductTreeByProductId(InsProductBindingDto insProductBindingDto) {
        if (insProductBindingDto.getInsProductId() == null) {
            throw new ErrorException("缺少检验项绑定id");
        }

        List<InsProduct> insProductBindingList = insProductBindingDto.getInsProductBindingList();
        // 查询原本检验项
        InsProduct insProduct = insProductMapper.selectById(insProductBindingDto.getInsProductId());

        for (InsProduct product : insProductBindingList) {
            product.setBindingProductId(insProductBindingDto.getInsProductId());
            product.setAsk(null);
            product.setTell(null);
            product.setPrice(null);
            product.setManHour(null);
            product.setSection(null);
            // 关联标识
            product.setRawMaterialTag(insProduct.getRawMaterialTag());
            product.setRepetitionTag(insProduct.getRepetitionTag());
            product.setInsSampleId(insProduct.getInsSampleId());
            product.setIsBinding(1);
            product.setState(1);
        }

        // 判断是否是电缆是电缆需要添加所有电缆关联关系
        if (StringUtils.isNotBlank(insProduct.getCableTag())) {
            // 查询其他电缆
            List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                    .eq(InsProduct::getInsSampleId, insProduct.getInsSampleId())
                    .eq(InsProduct::getStructureItemParameterId, insProduct.getStructureItemParameterId())
                    .isNotNull(InsProduct::getCableTag));
            for (InsProduct product : insProducts) {
                for (InsProduct insProduct1 : insProductBindingList) {
                    insProduct1.setId(null);
                    insProduct1.setBindingProductId(product.getId());
                    insProduct1.setCableTag(product.getCableTag());
                }
                this.saveBatch(insProductBindingList);
            }
        } else {
            this.saveBatch(insProductBindingList);
        }

        return true;
    }

    /**
     * 删除特殊检验项绑定信息
     * @param productId
     * @return
     */
    @Override
    public boolean removeBindingProductTree(Integer productId) {
        // 查询检验项
        InsProduct insProduct = insProductMapper.selectById(productId);
        // 判断是否是电缆配置, 需要删除关联的所有的检验项
        if (StringUtils.isNotBlank(insProduct.getCableTag())) {
            insProductMapper.delete(Wrappers.<InsProduct>lambdaQuery()
                    .eq(InsProduct::getInsSampleId, insProduct.getInsSampleId())
                    .eq(InsProduct::getIsBinding, 1)
                    .isNotNull(InsProduct::getBindingProductId)
                    .eq(InsProduct::getStructureItemParameterId, insProduct.getStructureItemParameterId())
                    .isNotNull(InsProduct::getCableTag));
        } else {
            insProductMapper.deleteById(productId);
        }
        return false;
    }


}




