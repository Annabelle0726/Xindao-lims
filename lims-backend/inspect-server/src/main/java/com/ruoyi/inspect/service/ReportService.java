package com.ruoyi.inspect.service;

import com.ruoyi.performance.dto.AuxiliaryAllDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;

import java.util.Map;

public interface ReportService {
    //首页-->日历任务图
    Map<String, Object> calendarWorkByWeek();

    AuxiliaryAllDto currentUserWorkHourCount(AuxiliaryOriginalHoursLookDto dto);
}
