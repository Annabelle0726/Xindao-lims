package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceMaintenance;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/4/17
 */
@Data
public class DeviceMaintenanceDto extends DeviceMaintenance {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;

}
