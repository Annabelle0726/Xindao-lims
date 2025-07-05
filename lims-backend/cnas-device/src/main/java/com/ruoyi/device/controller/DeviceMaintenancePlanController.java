package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceMaintenancePlanDto;
import com.ruoyi.device.pojo.DeviceMaintenancePlan;
import com.ruoyi.device.service.DeviceMaintenancePlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 设备保养计划表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:10:52
 */
@Api(tags = "设备保养计划")
@RestController
@RequestMapping("/deviceMaintenancePlan")
public class DeviceMaintenancePlanController {
    @Resource
    private DeviceMaintenancePlanService deviceMaintenancePlanService;

    /**
     * 分页查询设备保养计划
     * @return
     */
    @ApiOperation("分页查询设备保养计划")
    @GetMapping("selectDeviceMaintenancePlanByPage")
    public Result<IPage<DeviceMaintenancePlan>> selectDeviceMaintenancePlanByPage(Page page, DeviceMaintenancePlanDto itemParameter){
        return deviceMaintenancePlanService.selectDeviceMaintenancePlanByPage(page, itemParameter);
    }

    /**
     * 新增设备保养计划
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @ApiOperation("新增设备保养计划")
    @PostMapping("/addMaintenancePlan")
    public Result addMaintenancePlan(@RequestBody DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        return deviceMaintenancePlanService.addMaintenancePlan(deviceMaintenancePlanDto);
    }

    /**
     * 修改设备保养计划
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @ApiOperation("修改设备保养计划")
    @PostMapping("/updateMaintenancePlan")
    public Result updateMaintenancePlan(@RequestBody DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        return deviceMaintenancePlanService.updateMaintenancePlan(deviceMaintenancePlanDto);
    }

    /**
     * 删除设备保养计划
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @ApiOperation("删除设备保养计划")
    @DeleteMapping("/deleteMaintenancePlan")
    public Result deleteMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        return deviceMaintenancePlanService.deleteMaintenancePlan(deviceMaintenancePlanDto);
    }

    /**
     * 查询设备保养计划详情
     */
    @ApiOperation("查询设备保养计划详情")
    @GetMapping("/getMaintenancePlanDetail")
    public Result<DeviceMaintenancePlanDto> getMaintenancePlanDetail(Integer maintenancePlanId) {
        return deviceMaintenancePlanService.getMaintenancePlanDetail(maintenancePlanId);
    }

    /**
     * 导出设备保养计划
     */
    @ApiOperation("导出设备保养计划")
    @GetMapping("/exportDeviceMaintenancePlan")
    public Result exportDeviceMaintenancePlan(@RequestParam("maintenancePlanId") Integer maintenancePlanId, HttpServletResponse response) {
        return deviceMaintenancePlanService.exportDeviceMaintenancePlanDto(maintenancePlanId, response);
    }

    /**
     * 提交审核
     */
    @ApiOperation("提交审核")
    @PostMapping("/submitReviewMaintenancePlanStatus")
    public Result submitReviewMaintenancePlanStatus(@RequestBody DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        return deviceMaintenancePlanService.submitReviewMaintenancePlanStatus(deviceMaintenancePlanDto);
    }

    /**
     * 审核设备保养计划状态
     */
    @ApiOperation("审核设备保养计划状态")
    @PostMapping("/reviewMaintenancePlanStatus")
    public Result reviewMaintenancePlanStatus(@RequestBody DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        return deviceMaintenancePlanService.reviewMaintenancePlanStatus(deviceMaintenancePlanDto);
    }
}
