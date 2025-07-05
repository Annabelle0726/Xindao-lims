package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursTemporaryDto;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;

/**
 * 工时暂存表
 *
 * @author zhuo
 * @since 2025-02-27
 */
public interface AuxiliaryOutputWorkingHoursTemporaryService extends IService<AuxiliaryOutputWorkingHoursTemporary> {

    /**
     * 根据订单查询工时详情
     * @param page
     * @param workingHoursTemporary
     * @return
     */
    IPage<AuxiliaryOutputWorkingHoursTemporaryDto> getWorkingHoursByOrderId(Page page, AuxiliaryOutputWorkingHoursTemporaryDto workingHoursTemporary);
}

