package com.ruoyi.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.device.dto.DeviceStateDto;
import com.ruoyi.device.excel.DeviceStateExport;
import com.ruoyi.device.pojo.DeviceState;
import com.ruoyi.device.service.DeviceStateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 设备停用/启用 前端控制器
 * </p>
 *
 * @author
 * @since 2024-09-26 09:51:40
 */
@RestController
@RequestMapping("/deviceState")
public class DeviceStateController {

    @Autowired
    private DeviceStateService deviceStateService;

    @Autowired
    private NumberGenerator<DeviceState> numberGenerator;

    @PostMapping("saveDeviceState")
    public Result saveIncidentReportData(@RequestBody DeviceState deviceState) {
        if (ObjectUtils.isEmpty(deviceState.getProcessNumber())) {
            String year = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date());
            String month = new SimpleDateFormat("MM", Locale.CHINESE).format(new Date());
            String processNumber = numberGenerator.generateNumberWithPrefix(3, "DG-TC-23FM " + month + "-" + year + month, DeviceState::getProcessNumber);
            deviceState.setProcessNumber(processNumber);
        }
        deviceStateService.saveOrUpdate(deviceState);
        return Result.success();
    }

    @GetMapping("/getDeviceStatePage")
    public Result getDeviceStatePage(@RequestParam("deviceId") Integer deviceId, Page page, String processNumber){
        return Result.success(deviceStateService.getDeviceStatePage(deviceId, page, processNumber));
    }

    @DeleteMapping("/deleteDeviceState")
    public Result deleteDeviceState(@RequestParam("stateId") Integer stateId){
        return Result.success(deviceStateService.removeById(stateId));
    }

    @GetMapping("/deviceStateExport")
    public Result deviceStateExport(@RequestParam("deviceId") Integer deviceId, String processNumber, HttpServletResponse response) throws Exception {
        IPage<DeviceStateDto> deviceBorrows = deviceStateService.getDeviceStatePage(deviceId, new Page<>(-1, -1), processNumber);
        List<DeviceStateExport> studentList  = JSONObject.parseArray(JSON.toJSONString(deviceBorrows.getRecords()), DeviceStateExport.class);
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(DeviceStateExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet("sheet")
                .doWrite(studentList);
        return Result.success();
    }

    @ApiOperation(value = "设备启动/停止导出")
    @GetMapping("/exportDeviceStatus")
    public void exportDeviceStatus(@RequestParam("processNumber") String processNumber,@RequestParam("deviceId") Integer deviceId, HttpServletResponse response) throws Exception {
        deviceStateService.exportDeviceStatus(deviceId, processNumber, response);
    }
}
