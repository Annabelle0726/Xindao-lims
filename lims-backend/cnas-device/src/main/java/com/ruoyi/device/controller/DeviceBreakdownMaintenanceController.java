package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceBreakdownMaintenance;
import com.ruoyi.device.service.DeviceBreakdownMaintenanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 设备故障维修表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 04:50:57
 */
@Api(tags = "设备故障维修表")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceBreakdownMaintenance")
public class DeviceBreakdownMaintenanceController {

    private DeviceBreakdownMaintenanceService deviceBreakdownMaintenanceService;


    /**
     * 设备故障维修列表
     * @return
     */
    @ApiOperation(value = "设备故障维修列表")
    @GetMapping("/pageDeviceBreakdownMaintenance")
    public Result<IPage<DeviceBreakdownMaintenance>> pageDeviceBreakdownMaintenance(Page page, DeviceBreakdownMaintenance deviceBreakdownMaintenance) {
        return Result.success(deviceBreakdownMaintenanceService.pageDeviceBreakdownMaintenance(page, deviceBreakdownMaintenance));
    }

    /**
     * 查询设备故障维修
     * @return
     */
    @ApiOperation(value = "查询设备故障维修")
    @GetMapping("/getDeviceBreakdownMaintenance")
    public Result getDeviceBreakdownMaintenance(Integer maintenanceId){
        return Result.success(deviceBreakdownMaintenanceService.getById(maintenanceId));
    }

    /**
     * 删除设备故障维修
     * @return
     */
    @ApiOperation(value = "删除设备故障维修")
    @DeleteMapping("/delDeviceBreakdownMaintenance")
    public Result delDeviceBreakdownMaintenance(Integer maintenanceId){
        return Result.success(deviceBreakdownMaintenanceService.removeById(maintenanceId));
    }

    /**
     * 新增设备故障维修
     * @return
     */
    @ApiOperation(value = "新增设备故障维修")
    @PostMapping("/addDeviceBreakdownMaintenance")
    public Result addDeviceBreakdownMaintenance(@RequestBody DeviceBreakdownMaintenance deviceBreakdownMaintenance){
        return Result.success(deviceBreakdownMaintenanceService.addDeviceBreakdownMaintenance(deviceBreakdownMaintenance));
    }

    /**
     * 导出设备故障维修
     * @param maintenanceId 设备故障维修id
     * @param response 响应
     * @return
     */
    @ApiOperation(value = "导出设备故障维修")
    @GetMapping("/exportDeviceBreakdownMaintenance")
    public Result exportDeviceBreakdownMaintenance(Integer maintenanceId, HttpServletResponse response){
        deviceBreakdownMaintenanceService.exportDeviceBreakdownMaintenance(maintenanceId, response);
        return Result.success();
    }
}
