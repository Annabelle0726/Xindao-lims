package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.pojo.SupplierManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-15 02:46:45
 */
@Mapper
public interface SupplierManagementMapper extends BaseMapper<SupplierManagement> {

    IPage<SupplierManagement> pageSupplierManagement(Page page, @Param("ew") QueryWrapper<SupplierManagement> supplierManagementQueryWrapper);

    IPage<SupplierManagement> selectQualifiedSupplierManagement(Page page, @Param("ew") QueryWrapper<SupplierManagement> supplierManagementQueryWrapper);

    List<SupplierManagement> selectSupplierManagementAll(@Param("parentId") Integer parentId);

    List<SupplierManagement> selectSupplierManagement(@Param("parentId")Integer parentId);
}
