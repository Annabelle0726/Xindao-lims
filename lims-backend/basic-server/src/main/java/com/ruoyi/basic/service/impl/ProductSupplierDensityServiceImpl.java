package com.ruoyi.basic.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.ProductSupplierDensityMapper;
import com.ruoyi.basic.pojo.ProductSupplierDensity;
import com.ruoyi.basic.service.ProductSupplierDensityService;
import org.springframework.stereotype.Service;

/**
 * 产品厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-19
 */
@Service
public class ProductSupplierDensityServiceImpl extends ServiceImpl<ProductSupplierDensityMapper, ProductSupplierDensity> implements ProductSupplierDensityService {

    @Override
    public IPage<ProductSupplierDensity> selectByProductId(Page page, ProductSupplierDensity supplierDensity) {
        if (supplierDensity.getProductId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        return baseMapper.selectListByProductId(page, QueryWrappers.queryWrappers(supplierDensity), supplierDensity.getProductId());
    }

    @Override
    public void addProductSupplierDensity(ProductSupplierDensity supplierDensity) {
        if (supplierDensity.getProductId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        baseMapper.insert(supplierDensity);
    }

    @Override
    public void updateProductSupplierDensity(ProductSupplierDensity supplierDensity) {
        if (supplierDensity.getProductId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        baseMapper.updateById(supplierDensity);
    }
}

