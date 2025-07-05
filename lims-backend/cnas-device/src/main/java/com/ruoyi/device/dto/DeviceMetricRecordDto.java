package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceMetricRecord;
import com.ruoyi.device.pojo.DeviceMetricsCopy;
import lombok.Data;

import java.util.List;

@Data
public class DeviceMetricRecordDto extends DeviceMetricRecord {

    private List<DeviceMetricsCopy> deviceMetricsCopyList;
}
