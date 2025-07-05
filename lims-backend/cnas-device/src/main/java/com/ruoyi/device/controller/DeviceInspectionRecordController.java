package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceInspectionRecordDto;
import com.ruoyi.device.pojo.DeviceInspectionRecord;
import com.ruoyi.device.service.DeviceInspectionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 *  设备点检记录表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:25:14
 */
@Api(tags = "设备点检记录")
@RestController
@RequestMapping("/deviceInspectionRecord")
public class DeviceInspectionRecordController {
    @Resource
    private DeviceInspectionRecordService deviceInspectionRecordService;

    /**
     * 分页查询设备点检记录
     */
    @ApiOperation("分页查询设备点检记录")
    @GetMapping("/getDeviceInspectionRecordByPage")
    public Result<IPage<DeviceInspectionRecord>> getDeviceInspectionRecordByPage(Page page, DeviceInspectionRecordDto itemParameter) {
        return deviceInspectionRecordService.getDeviceInspectionRecordByPage(page, itemParameter);
    }

    /**
     * 查询点检详情
     */
    @ApiOperation("查询点检详情")
    @GetMapping("/getDeviceInspectionRecord")
    public Result getDeviceInspectionRecord(Integer inspectionRecordId) {
        return deviceInspectionRecordService.getDeviceInspectionRecord(inspectionRecordId);
    }

    /**
     * 新增设备点检记录
     * @param deviceInspectionRecord  设备点检记录
     */
    @ApiOperation("新增设备点检记录")
    @PostMapping("/addDeviceInspectionRecord")
    public Result addDeviceInspectionRecord(@RequestBody DeviceInspectionRecordDto deviceInspectionRecord) {
        return deviceInspectionRecordService.addDeviceInspectionRecord(deviceInspectionRecord);
    }

    /**
     * 修改设备点检记录
     */
    @ApiOperation("修改设备点检记录")
    @PostMapping("/updateDeviceInspectionRecord")
    public Result updateDeviceInspectionRecord(@RequestBody DeviceInspectionRecordDto deviceInspectionRecord) {
        return deviceInspectionRecordService.updateInspectionRecordAndDetails(deviceInspectionRecord);
    }

    /**
     * 删除设备点检记录
     */
    @ApiOperation("删除设备点检记录")
    @DeleteMapping("/deleteDeviceInspectionRecord")
    public Result deleteDeviceInspectionRecord(DeviceInspectionRecordDto deviceInspectionRecord) {
        return deviceInspectionRecordService.deleteDeviceInspectionRecordOrDetails(deviceInspectionRecord);
    }


    /**
     * 复核点检记录
     * @return
     */
    @ApiOperation(value = "复核核查记录")
    @PostMapping("/reviewDeviceInspectionRecord")
    public Result reviewDeviceInspectionRecord(@RequestBody DeviceInspectionRecordDto deviceExamineRecordDto){
        return deviceInspectionRecordService.reviewDeviceInspectionRecord(deviceExamineRecordDto);
    }


    /**
     * 导出设备点检记录
     */
    @ApiOperation("导出设备点检记录")
    @GetMapping("/exportDeviceInspectionRecord")
    public Result exportDeviceInspectionRecord(@RequestParam("inspectionRecordId") Integer inspectionRecordId, HttpServletResponse response) {
        return deviceInspectionRecordService.exportDeviceInspectionRecord(inspectionRecordId, response);
    }
}
