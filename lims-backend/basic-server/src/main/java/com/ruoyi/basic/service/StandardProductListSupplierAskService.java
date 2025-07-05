package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StandardProductListSupplierAsk;

import java.util.List;

/**
 * 检验项目厂家密度绑定表
 *
 * @author makejava
 * @since 2024-09-23
 */
public interface StandardProductListSupplierAskService extends IService<StandardProductListSupplierAsk> {

    List<StandardProductListSupplierAsk> selectByProductId(StandardProductListSupplierAsk supplierAsk);

    Integer addProductSupplierAsk(StandardProductListSupplierAsk supplierAsk);

    Integer updateProductSupplierAsk(StandardProductListSupplierAsk supplierAsk);

}

