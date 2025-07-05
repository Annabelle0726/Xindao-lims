package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceRecordDto extends DeviceRecord {

    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("设备编号")
    private String managementNumber;
}
