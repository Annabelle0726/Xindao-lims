package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.device.pojo.DeviceRecord;
import com.ruoyi.device.service.DeviceRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * cnas设备使用记录表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 11:06:47
 */
@Api(tags = "设备使用记录")
@RestController
@RequestMapping("/deviceRecord")
public class DeviceRecordController {
    @Autowired
    private DeviceRecordService deviceRecordService;

    @ApiOperation(value = "备使用记录查询")
    @GetMapping("/deviceRecordPage")
    public Result deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber) {
        return Result.success(deviceRecordService.deviceRecordPage(deviceId, page, sampleCode, managementNumber));
    }

    @ApiOperation(value = "新增")
    @PostMapping("/saveDeviceRecord")
    public Result saveDeviceRecords(@RequestBody DeviceRecord deviceRecord) {
        return Result.success(deviceRecordService.save(deviceRecord));
    }

    /**
     * 编辑设备使用记录
     * @param deviceRecord
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateDeviceRecord")
    public Result updateDeviceRecord(@RequestBody DeviceRecord deviceRecord) {
        return Result.success(deviceRecordService.updateById(deviceRecord));
    }

    @DeleteMapping("/deleteDeviceRecord")
    public Result deleteDeviceRecords(@RequestParam("id") Integer id) {
        return Result.success(deviceRecordService.removeById(id));
    }

    @ApiOperation(value = "设备使用记录导出")
    @GetMapping("/exportUseRecord")
    public void exportUseRecord(Integer deviceId, String exportDate, HttpServletResponse response){
        deviceRecordService.exportUseRecord(deviceId, exportDate, response);
    }
}
