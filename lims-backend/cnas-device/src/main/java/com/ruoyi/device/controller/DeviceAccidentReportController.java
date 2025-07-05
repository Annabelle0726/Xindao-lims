package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceAccidentReport;
import com.ruoyi.device.service.DeviceAccidentReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 设备事故报告单 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 06:31:12
 */
@Api(tags = "设备事故报告单")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceAccidentReport")
public class DeviceAccidentReportController {

    private DeviceAccidentReportService deviceAccidentReportService;


    /**
     * 设备事故报告列表
     * @return
     */
    @ApiOperation(value = "设备事故报告列表")
    @GetMapping("/pageDeviceAccidentReport")
    public Result<IPage<DeviceAccidentReport>> pageDeviceAccidentReport(Page page, DeviceAccidentReport deviceAccidentReport){
        return Result.success(deviceAccidentReportService.pageDeviceAccidentReport(page, deviceAccidentReport));
    }

    /**
     * 查询设备事故报告
     * @return
     */
    @ApiOperation(value = "查询设备事故报告")
    @GetMapping("/getDeviceAccidentReport")
    public Result getDeviceAccidentReport(Integer accidentReportId){
        return Result.success(deviceAccidentReportService.getById(accidentReportId));
    }

    /**
     * 删除设备事故报告
     * @return
     */
    @ApiOperation(value = "删除设备事故报告")
    @DeleteMapping("/delDeviceAccidentReport")
    public Result delDeviceAccidentReport(Integer accidentReportId){
        return Result.success(deviceAccidentReportService.removeById(accidentReportId));
    }

    /**
     * 新增设备事故报告
     * @return
     */
    @ApiOperation(value = "新增设备事故报告")
    @PostMapping("/addDeviceAccidentReport")
    public Result addDeviceAccidentReport(@RequestBody DeviceAccidentReport deviceAccidentReport){
        return Result.success(deviceAccidentReportService.addDeviceAccidentReport(deviceAccidentReport));
    }

    /**
     * 导出设备事故报告
     * @param accidentReportId 设备事故报告id
     * @param response 响应
     */
    @ApiOperation(value = "导出设备事故报告")
    @GetMapping("/exportDeviceAccidentReport")
    public Result exportDeviceAccidentReport(Integer accidentReportId, HttpServletResponse response) {
        deviceAccidentReportService.exportDeviceAccidentReport(accidentReportId, response);
        return Result.success();
    }
}
