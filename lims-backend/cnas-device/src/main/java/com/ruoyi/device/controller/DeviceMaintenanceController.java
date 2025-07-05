package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.device.dto.DeviceMaintenanceDto;
import com.ruoyi.device.pojo.DeviceMaintenance;
import com.ruoyi.device.service.DeviceMaintenanceService;
import com.ruoyi.system.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * todo: 孙河滨
 */
@RestController
@Api(tags = "设备维护保养")
@RequestMapping("/deviceMaintain")
public class DeviceMaintenanceController {

    @Autowired
    private DeviceMaintenanceService deviceMaintenanceService;

    @Autowired
    private UserMapper userMapper;


    /**
     * 设备维护分页查询
     * @param deviceMaintenance
     * @return
     */
    @ApiOperation(value = "设备维护分页查询")
    @GetMapping("/selectDeviceMaintenancePage")
    public Result selectDeviceMaintenancePage(Page page, DeviceMaintenanceDto deviceMaintenance){
        return Result.success(deviceMaintenanceService.selectDeviceMaintenancePage(page, deviceMaintenance));
    }


    /**
     * 新增修改设备维护保养
     * @param deviceMaintenance
     * @return
     */
    @ApiOperation(value = "新增修改设备维护保养")
    @PostMapping("/addDeviceMaintenance")
    public Result addDeviceMaintenance(@RequestBody DeviceMaintenance deviceMaintenance){
        if (deviceMaintenance.getDeviceId() == null) {
            throw new RuntimeException("缺少设备id");
        }
        User user = userMapper.selectById(deviceMaintenance.getMaintenanceUserId());
        deviceMaintenance.setMaintenanceUserName(user.getName());

        return Result.success(deviceMaintenanceService.saveOrUpdate(deviceMaintenance));
    }

    /**
     * 删除修改设备维护保养
     * @param id
     * @return
     */
    @ApiOperation(value = "删除修改设备维护保养")
    @DeleteMapping("/deleteDeviceMaintenance")
    public Result deleteDeviceMaintenance(Integer id){
        return Result.success(deviceMaintenanceService.removeById(id));
    }

    /**
     * 导出设备维护保养
     */
    @ApiOperation("导出设备维护保养")
    @GetMapping("/exportDeviceMaintenance")
    public void exportDeviceMaintenance(@RequestParam("deviceId") Integer deviceId, HttpServletResponse response) {
        deviceMaintenanceService.exportDeviceMaintenance(deviceId, response);
    }

}
