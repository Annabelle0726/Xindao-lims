package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceMetric;
import com.ruoyi.device.service.IDeviceMetricService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/deviceMetrics")
public class DeviceMetricController {

    @Autowired
    private IDeviceMetricService deviceMetricService;

    @PostMapping("/saveOrUpdateDeviceMetric")
    public Result create(@RequestBody DeviceMetric deviceMetric) {
        return Result.success(deviceMetricService.saveOrUpdate(deviceMetric));
    }

    @GetMapping("/selectDeviceMetric")
    public Result read(@RequestParam("deviceId") Integer deviceId, @RequestParam("type") String type) {
        return Result.success(deviceMetricService.list(Wrappers.<DeviceMetric>lambdaQuery()
                .eq(DeviceMetric::getDeviceId,deviceId)
                .eq(DeviceMetric::getType,type)));
    }

    @DeleteMapping("/deleteDeviceMetrics")
    public Result delete(@RequestParam("id") Integer id) {
      return   Result.success(deviceMetricService.removeById(id));
    }
}
