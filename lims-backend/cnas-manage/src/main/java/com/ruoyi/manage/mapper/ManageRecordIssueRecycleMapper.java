package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordIssueRecycle;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 所有文件(内、外部文件)的发放与回收记录 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-13 09:11:05
 */
public interface ManageRecordIssueRecycleMapper extends BaseMapper<ManageRecordIssueRecycle> {

    IPage<ManageRecordIssueRecycle> pageManageRecordIssueRecycle(Page page, @Param("ew") QueryWrapper<ManageRecordIssueRecycle> queryWrappers);
}
