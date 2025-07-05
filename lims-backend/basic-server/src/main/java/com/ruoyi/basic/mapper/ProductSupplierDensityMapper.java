package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.ProductSupplierDensity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-19
 */
@Mapper
public interface ProductSupplierDensityMapper extends BaseMapper<ProductSupplierDensity> {

    IPage<ProductSupplierDensity> selectListByProductId(@Param("page") Page page, @Param("ew") QueryWrapper<ProductSupplierDensity> ew, @Param("productId") Integer productId);
}

