package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.FileSaveUtil;
import com.ruoyi.device.dto.DeviceMetricRecordDto;
import com.ruoyi.device.pojo.DeviceMetricRecord;
import com.ruoyi.device.pojo.DeviceMetricsCopy;
import com.ruoyi.device.service.DeviceMetricRecordService;
import com.ruoyi.device.service.DeviceMetricsCopyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 设备校准 - 校准记录 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-09-27 10:20:01
 */
@Api(tags = "设备 - 设备校准")
@RestController
@RequestMapping("/deviceMetricRecord")
public class DeviceMetricRecordController {

    @Autowired
    private DeviceMetricRecordService deviceMetricRecordService;

    @Autowired
    private DeviceMetricsCopyService deviceMetricsCopyService;

    @Autowired
    private NumberGenerator<DeviceMetricRecord> numberGenerator;

    @ApiOperation("设备校准分页查询")
    @GetMapping("/deviceMetricRecordPage")
    public Result deviceMetricRecordPage(@RequestParam("deviceId") Integer deviceId, Page page, @RequestParam("type") String type) {
        return Result.success(deviceMetricRecordService.page(page, Wrappers.<DeviceMetricRecord>lambdaQuery()
                .eq(DeviceMetricRecord::getDeviceId, deviceId)
                .eq(DeviceMetricRecord::getType, type)));
    }

    @ApiOperation("设备校准 新增 更新")
    @PostMapping("/addOrUpdateDeviceMetricRecord")
    public Result addOrUpdateDeviceMetricRecord(@RequestBody DeviceMetricRecordDto deviceMetricRecordDto) {
        if (ObjectUtils.isEmpty(deviceMetricRecordDto.getProcessNumber())) {
            String year = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date());
            String month = new SimpleDateFormat("MM", Locale.CHINESE).format(new Date());
            String processNumber = numberGenerator.generateNumberWithPrefix(5, "SBJZ" + year + month, DeviceMetricRecord::getProcessNumber);
            deviceMetricRecordDto.setProcessNumber(processNumber);
        }
        deviceMetricRecordService.saveOrUpdate(deviceMetricRecordDto);
        if (CollectionUtils.isNotEmpty(deviceMetricRecordDto.getDeviceMetricsCopyList())) {
            deviceMetricRecordDto.getDeviceMetricsCopyList().forEach(i -> i.setDeviceMetricsId(deviceMetricRecordDto.getId()));
            deviceMetricsCopyService.saveOrUpdateBatch(deviceMetricRecordDto.getDeviceMetricsCopyList());
        }
        return Result.success();
    }

    @ApiOperation("设备校准 删除")
    @DeleteMapping("/deleteDeviceMetricRecord")
    public Result deleteDeviceMetricRecord(@RequestParam("id") Integer id) {
        DeviceMetricRecord deviceMetricRecord = deviceMetricRecordService.getById(id);
        deviceMetricsCopyService.remove(Wrappers.<DeviceMetricsCopy>lambdaQuery()
                .eq(DeviceMetricsCopy::getDeviceMetricsId, id));
        // 删除文件
        FileSaveUtil.DeleteFile(deviceMetricRecord.getSystemFileName());
        return Result.success(deviceMetricRecordService.removeById(id));
    }

    @ApiOperation("设备校准 校准条目查询")
    @GetMapping("/showDeviceMetricsCopy")
    public Result showDeviceMetricsCopy(@RequestParam("id") Integer id, @RequestParam("type") String type) {
        return Result.success(deviceMetricsCopyService.list(Wrappers.<DeviceMetricsCopy>lambdaQuery()
                .eq(DeviceMetricsCopy::getDeviceMetricsId, id)
                .eq(DeviceMetricsCopy::getType, type)));
    }
}
