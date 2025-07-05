package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursTemporaryDto;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;
import org.apache.ibatis.annotations.Param;

/**
 * 工时暂存表
 *
 * @author zhuo
 * @since 2025-02-27
 */
public interface AuxiliaryOutputWorkingHoursTemporaryMapper extends BaseMapper<AuxiliaryOutputWorkingHoursTemporary> {

    /**
     * 根据订单查询工时详情
     * @param page
     * @return
     */
    IPage<AuxiliaryOutputWorkingHoursTemporaryDto> getWorkingHoursByOrderId(Page page, @Param("ew") QueryWrapper<AuxiliaryOutputWorkingHoursTemporary> ew);
}

