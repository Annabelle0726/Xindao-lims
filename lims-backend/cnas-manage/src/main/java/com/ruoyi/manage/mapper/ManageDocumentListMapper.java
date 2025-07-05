package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageDocumentList;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件清单
 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-08 11:08:11
 */
public interface ManageDocumentListMapper extends BaseMapper<ManageDocumentList> {

    IPage<ManageDocumentList> pageManageDocumentList(Page page, @Param("ew") QueryWrapper<ManageDocumentList> queryWrappers);
}
