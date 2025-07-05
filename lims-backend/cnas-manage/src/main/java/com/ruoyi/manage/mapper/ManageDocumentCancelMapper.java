package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageDocumentCancel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件作废 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-09 02:37:35
 */
public interface ManageDocumentCancelMapper extends BaseMapper<ManageDocumentCancel> {

    IPage<ManageDocumentCancel> pageManageDocumentCancel(Page page, @Param("ew") QueryWrapper<ManageDocumentCancel> queryWrappers);

    ManageDocumentCancel getManageDocumentCancel(Integer id);
}
