package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordIntervals;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件定期审查记录 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-13 10:54:31
 */
public interface ManageRecordIntervalsMapper extends BaseMapper<ManageRecordIntervals> {

    IPage<ManageRecordIntervals> pageManageRecordIntervals(Page page, @Param("ew") QueryWrapper<ManageRecordIntervals> queryWrappers);
}
