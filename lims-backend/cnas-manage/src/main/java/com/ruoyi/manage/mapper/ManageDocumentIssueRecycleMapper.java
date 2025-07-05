package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.ManageDocumentIssueRecycleDto;
import com.ruoyi.manage.pojo.ManageDocumentIssueRecycle;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件发放回收 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-09 09:18:24
 */
public interface ManageDocumentIssueRecycleMapper extends BaseMapper<ManageDocumentIssueRecycle> {

    IPage<ManageDocumentIssueRecycleDto> pageManageDocumentIssueRecycle(Page page, @Param("ew") QueryWrapper<ManageDocumentIssueRecycleDto> queryWrappers);

    ManageDocumentIssueRecycleDto getManageDocumentIssueRecycle(Long id);
}
