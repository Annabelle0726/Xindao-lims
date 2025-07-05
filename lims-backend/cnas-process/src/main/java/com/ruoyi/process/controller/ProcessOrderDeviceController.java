package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceRecord;
import com.ruoyi.device.service.DeviceRecordService;
import com.ruoyi.process.pojo.ProcessOrderDevice;
import com.ruoyi.process.service.ProcessOrderDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * cnas设备使用记录表(7.1检验委托单) 前端控制器
 * </p>
 *
 * @author
 * @since 2025-04-17 03:51:48
 */
@Api(tags = "cnas设备使用记录")
@RestController
@RequestMapping("/processOrderDevice")
public class ProcessOrderDeviceController {
    @Autowired
    private ProcessOrderDeviceService processOrderDeviceService;

    @ApiOperation(value = "设备使用记录查询")
    @GetMapping("/deviceRecordPage")
    public Result deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber) {
        return Result.success(processOrderDeviceService.deviceRecordPage(deviceId, page, sampleCode, managementNumber));
    }

    @ApiOperation(value = "新增")
    @PostMapping("/saveDeviceRecord")
    public Result saveDeviceRecords(@RequestBody ProcessOrderDevice processOrderDevice) {
        return Result.success(processOrderDeviceService.save(processOrderDevice));
    }

    /**
     * 编辑设备使用记录
     * @param processOrderDevice
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateDeviceRecord")
    public Result updateDeviceRecord(@RequestBody ProcessOrderDevice processOrderDevice) {
        return Result.success(processOrderDeviceService.updateById(processOrderDevice));
    }

    @DeleteMapping("/deleteDeviceRecord")
    public Result deleteDeviceRecords(@RequestParam("id") Integer id) {
        return Result.success(processOrderDeviceService.removeById(id));
    }

    @ApiOperation(value = "设备使用记录导出")
    @GetMapping("/exportUseRecord")
    public void exportUseRecord(Integer deviceId, String exportDate, HttpServletResponse response){
        //processOrderDeviceService.exportUseRecord(deviceId, exportDate, response);
    }
}
