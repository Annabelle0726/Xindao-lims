package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordIntervalsTotal;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件定期审查记录总历史记录表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-15 01:12:11
 */
public interface ManageRecordIntervalsTotalMapper extends BaseMapper<ManageRecordIntervalsTotal> {

    IPage<ManageRecordIntervalsTotal> pageManageRecordIntervalsTotal(Page page, @Param("ew") QueryWrapper<ManageRecordIntervalsTotal> queryWrappers);
}
