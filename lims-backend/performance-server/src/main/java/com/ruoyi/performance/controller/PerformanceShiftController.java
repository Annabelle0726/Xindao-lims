package com.ruoyi.performance.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.performance.dto.PerformanceShiftAddDto;
import com.ruoyi.performance.pojo.PerformanceShift;
import com.ruoyi.performance.service.PerformanceShiftService;
import com.ruoyi.performance.utils.StyleMonthUtils;
import com.ruoyi.performance.utils.StyleYearUtils;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 绩效管理-班次 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-08 09:12:04
 */
@Api(tags = "绩效管理-班次")
@RestController
@RequestMapping("/performanceShift")
public class PerformanceShiftController {

    @Autowired
    private PerformanceShiftService performanceShiftService;

    @Resource
    private ISysDictTypeService dictTypeService;

    @Resource
    private UserService userService;

    @ApiOperation(value = "排班")
    @PostMapping("add")
    public Result<?> performanceShiftAdd(@RequestBody PerformanceShiftAddDto performanceShiftAddDto) {
        performanceShiftService.performanceShiftAdd(performanceShiftAddDto);
        return Result.success();
    }

    @ApiOperation(value = "月份分页查询")
    @GetMapping("page")
    public Result<?> performanceShiftPage(Integer size, Integer current, String time, String userName, String laboratory) {
        return Result.success(performanceShiftService.performanceShiftPage(new Page<>(current, size), time, userName, laboratory));
    }

    @ApiOperation(value = "年份分页查询")
    @GetMapping("pageYear")
    public Result<?> performanceShiftPageYear(Integer size, Integer current, String time, String userName, String laboratory) {
        return Result.success(performanceShiftService.performanceShiftPageYear(new Page<>(current, size), time, userName, laboratory));
    }

    @ApiOperation(value = "班次状态修改")
    @PostMapping("update")
    public Result<?> performanceShiftUpdate(@RequestBody PerformanceShift performanceShift) {
        performanceShiftService.performanceShiftUpdate(performanceShift);
        return Result.success();
    }

    @ApiOperation(value = "导出")
    @GetMapping("export")
    public void exportToExcel(@NotNull(message = "时间不能为空！") String time, String userName, String laboratory, Boolean isMonth, HttpServletResponse response) throws Exception {
        Map<Object, Object> data;
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("requestType","excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        if (!isMonth) {
            data = performanceShiftService.exportToYearExcel(time, userName, laboratory);
            // 设置单元格样式
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(StyleYearUtils.getHeadStyle(), StyleYearUtils.getContentStyle());
            // 保存到第一个sheet中
            EasyExcel.write(response.getOutputStream())
                    .head((List<List<String>>) data.get("header"))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet("年度")
                    .doWrite((Collection<?>) data.get("data"));
        } else {
            data = performanceShiftService.exportToMonthExcel(time, userName, laboratory);
            // 设置单元格样式
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(StyleMonthUtils.getHeadStyle(), StyleMonthUtils.getContentStyle());
            EasyExcel.write(response.getOutputStream())
                    .head((List<List<String>>) data.get("header"))
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet("月度")
                    .doWrite((Collection<?>) data.get("data"));
        }
    }

    @ApiOperation(value = "临时接口-添加7月份8月份的数据")
    @GetMapping("temporaryInterface")
    public void temporaryInterface() {
        // TODO 给每个人都进行排班(默认早班)
        PerformanceShiftAddDto performanceShiftAddDto = new PerformanceShiftAddDto();
        //班次--早(查询字典)
        List<SysDictData> shiftType = dictTypeService.selectDictDataByName("班次类型");
        List<String> collect = shiftType.stream().filter(enums -> enums.getDictLabel().equals("早")).map(enums -> enums.getDictValue()).collect(Collectors.toList());
        performanceShiftAddDto.setShift(collect.get(0));
        //人员--所有人
        String userIds = userService.list().stream().map(user -> user.getId().toString()).distinct().collect(Collectors.joining(","));
        performanceShiftAddDto.setUserId(userIds);
        //周次--当月所有
        // 获取当前日期
        LocalDate today = LocalDate.of(2024, 8, 15);
        // 获取本月的第一天和最后一天
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        // 获取周字段信息（根据区域设置）
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        // 获取本月第一天的周一
        LocalDate startOfWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
        // 遍历本月所有天数，找出每周的第一天和最后一天
        LocalDate endOfWeek;
        while (startOfWeek.isBefore(firstDayOfMonth.plusMonths(1))) {
            endOfWeek = startOfWeek.plusDays(6);
            LocalDateTime startDateTime = LocalDateTime.of(startOfWeek, LocalTime.MIDNIGHT);
            LocalDateTime endDateTime = LocalDateTime.of(endOfWeek, LocalTime.MIDNIGHT);
            performanceShiftAddDto.setStartWeek(startDateTime);
            performanceShiftAddDto.setEndWeek(endDateTime);
            performanceShiftService.performanceShiftAdd(performanceShiftAddDto);
            startOfWeek = startOfWeek.plusWeeks(1);
        }

    }
}
