package com.ruoyi.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.ProductPartMapper;
import com.ruoyi.basic.mapper.StructureTestObjectPartMapper;
import com.ruoyi.basic.pojo.ProductPart;
import com.ruoyi.basic.pojo.StructureTestObjectPart;
import com.ruoyi.basic.service.ProductPartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class ProductPartServiceImpl extends ServiceImpl<ProductPartMapper, ProductPart> implements ProductPartService {

    private  ProductPartMapper productPartMapper;
    private StructureTestObjectPartMapper structureTestObjectPartMapper;


    @Override
    public IPage<ProductPart> selectByProductId(IPage<ProductPart> page,ProductPart productPart) {
        return productPartMapper.selectListByProductId(page, QueryWrappers.queryWrappers(productPart),productPart.getProductId());
    }

    @Override
    public void addProductPart(ProductPart productPart) {
        if (productPart.getProductId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        this.isPartNoExist(productPart.getPartNo(), productPart.getProductId(), null);
        productPartMapper.insert(productPart);
    }

    @Override
    public void updateProductPartById(ProductPart productPart) {
        this.isPartNoExist(productPart.getPartNo(), productPart.getProductId(), productPart.getId());
        if (productPart.getProductId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        productPartMapper.updateById(productPart);
    }

    // 判断零件号是否存在
    public void isPartNoExist(String partNo,Integer productId, Integer id) {
        // 零件号唯一 但不必填
        if (StringUtils.isNotBlank(partNo)) {
            Long count = productPartMapper.selectCount(new LambdaQueryWrapper<ProductPart>()
//                    .eq(ProductPart::getProductId, productId)
                    .eq(ProductPart::getPartNo, partNo)
                    .ne(id != null, ProductPart::getId, id));
            Long selectCount = structureTestObjectPartMapper.selectCount(Wrappers.<StructureTestObjectPart>lambdaQuery()
                    .eq(StructureTestObjectPart::getPartNo, partNo));
            if (count > 0 || selectCount > 0) {
                throw new BaseException("该零件号已绑定过检验对象");
            }
        } else {
            throw new BaseException("请输入零件号");
        }
    }
}
