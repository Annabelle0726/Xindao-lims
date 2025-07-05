package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.SupplierManagement;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-11-15 02:46:45
 */
public interface SupplierManagementService extends IService<SupplierManagement> {

    List<SupplierManagement> selectSupplierManagement(SupplierManagement supplierManagement);

    void exportSupplierManagement(Integer parentId, HttpServletResponse response);

    IPage<SupplierManagement> selectQualifiedSupplierManagement(Page page, SupplierManagement supplierManagement);

    List<SupplierManagement> selectQualifiedSupplierManagementById(Integer supplierManagementId);

    List<SupplierManagement> selectSupplierManagementByParentId(Integer parentId);

    List<SupplierManagement> selectSupplierManagementAll();
}
