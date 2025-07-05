package com.ruoyi.basic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.basic.mapper.StandardProductListSupplierAskMapper;
import com.ruoyi.basic.pojo.StandardProductListSupplierAsk;
import com.ruoyi.basic.service.StandardProductListSupplierAskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 检验项目厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-23
 */
@Service
public class StandardProductListSupplierAskServiceImpl extends ServiceImpl<StandardProductListSupplierAskMapper, StandardProductListSupplierAsk> implements StandardProductListSupplierAskService {

    /**
     * 查询厂家要求值绑定
     * @param supplierAsk
     * @return
     */
    @Override
    public List<StandardProductListSupplierAsk> selectByProductId(StandardProductListSupplierAsk supplierAsk) {
        if (supplierAsk.getProductListId() == null) {
            throw new BaseException("缺少检验项id");
        }
        return baseMapper.selectList(Wrappers.<StandardProductListSupplierAsk>lambdaQuery()
                .eq(StandardProductListSupplierAsk::getProductListId, supplierAsk.getProductListId()));
    }

    /**
     * 新增厂家要求值绑定
     * @param supplierAsk
     * @return
     */
    @Override
    public Integer addProductSupplierAsk(StandardProductListSupplierAsk supplierAsk) {
        if (supplierAsk.getProductListId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        // 查询改厂家是否绑定过
        Long count = baseMapper.selectCount(Wrappers.<StandardProductListSupplierAsk>lambdaQuery()
                .eq(StandardProductListSupplierAsk::getProductListId, supplierAsk.getProductListId())
                .eq(StandardProductListSupplierAsk::getSupplierName, supplierAsk.getSupplierName()));
        if (count > 0){
            throw new BaseException("该产品已绑定过该厂家");
        }
        baseMapper.insert(supplierAsk);
        return supplierAsk.getSupplierAskId();
    }

    /**
     * 修改厂家要求值绑定
     * @param supplierAsk
     * @return
     */
    @Override
    public Integer updateProductSupplierAsk(StandardProductListSupplierAsk supplierAsk) {
        if (supplierAsk.getProductListId() == null) {
            throw new BaseException("缺少检验项id");
        }
        // 查询改厂家是否绑定过
        Long count = baseMapper.selectCount(Wrappers.<StandardProductListSupplierAsk>lambdaQuery()
                .ne(StandardProductListSupplierAsk::getSupplierAskId, supplierAsk.getSupplierAskId())
                .eq(StandardProductListSupplierAsk::getProductListId, supplierAsk.getProductListId())
                .eq(StandardProductListSupplierAsk::getSupplierName, supplierAsk.getSupplierName()));
        if (count > 0){
            throw new BaseException("该检验项已绑定过该厂家");
        }
        baseMapper.updateById(supplierAsk);
        return supplierAsk.getSupplierAskId();
    }
}

