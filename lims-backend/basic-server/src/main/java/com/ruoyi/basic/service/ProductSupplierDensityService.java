package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.ProductSupplierDensity;

/**
 * 产品厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-19
 */
public interface ProductSupplierDensityService extends IService<ProductSupplierDensity> {

    IPage<ProductSupplierDensity> selectByProductId(Page page, ProductSupplierDensity supplierDensity);

    void addProductSupplierDensity(ProductSupplierDensity supplierDensity);

    void updateProductSupplierDensity(ProductSupplierDensity supplierDensity);
}

