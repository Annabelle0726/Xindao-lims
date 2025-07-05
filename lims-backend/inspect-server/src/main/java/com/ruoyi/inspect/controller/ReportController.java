package com.ruoyi.inspect.controller;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.service.ReportService;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
@Api(tags = "报表模块")
public class ReportController {

    private ReportService reportService;

    @ApiOperation(value = "查询日历任务图")
    @GetMapping("/calendarWorkByWeek")
    public Result calendarWorkByWeek(){
        return Result.success(reportService.calendarWorkByWeek());
    }

    @ApiOperation(value = "当前用户工时统计")
    @GetMapping("/currentUserWorkHourCount")
    public Result currentUserWorkHourCount(AuxiliaryOriginalHoursLookDto dto){
        return Result.success(reportService.currentUserWorkHourCount(dto));
    }

}
