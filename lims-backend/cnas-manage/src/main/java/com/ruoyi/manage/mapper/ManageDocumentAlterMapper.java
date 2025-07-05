package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageDocumentAlter;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件变更 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-11 11:04:01
 */
public interface ManageDocumentAlterMapper extends BaseMapper<ManageDocumentAlter> {

    IPage<ManageDocumentAlter> pageManageDocumentAlter(Page page, @Param("ew") QueryWrapper<ManageDocumentAlter> queryWrappers);

    ManageDocumentAlter getManageDocumentAlter(Integer id);

}
