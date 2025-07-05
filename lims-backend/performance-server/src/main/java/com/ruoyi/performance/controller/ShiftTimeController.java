package com.ruoyi.performance.controller;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.performance.pojo.ShiftTime;
import com.ruoyi.performance.service.ShiftTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 班次对应的时间 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-24 11:22:17
 */
@Api(tags = "绩效管理-班次排班")
@RestController
@RequestMapping("/shiftTime")
public class ShiftTimeController {

    @Autowired
    private ShiftTimeService shiftTimeService;

    @ApiOperation(value = "排班时间配置")
    @PostMapping("add")
    public Result<?> shiftTimeAdd(@RequestBody ShiftTime shiftTime) {
        shiftTimeService.shiftTimeAdd(shiftTime);
        return Result.success();
    }

    @ApiOperation(value = "排班时间配置查询")
    @GetMapping("list")
    public Result<?> shiftTimeList() {
        return Result.success(shiftTimeService.shiftTimeList());
    }

    @ApiOperation(value = "排班时间配置删除")
    @DeleteMapping("remove")
    public Result<?> shiftTimeRemove(Long id) {
        return Result.success(shiftTimeService.removeById(id));
    }

    @ApiOperation(value = "排班时间配置修改")
    @PostMapping("update")
    public Result<?> shiftTimeUpdate(@RequestBody ShiftTime shiftTime) {
        return Result.success(shiftTimeService.updateById(shiftTime));
    }

}
