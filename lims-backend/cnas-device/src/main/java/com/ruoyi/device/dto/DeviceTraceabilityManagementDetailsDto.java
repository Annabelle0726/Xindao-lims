package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceTraceabilityManagementDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-20 星期五 15:05:02
 * Description:
 */
@Data
public class DeviceTraceabilityManagementDetailsDto extends DeviceTraceabilityManagementDetails {
    @ApiModelProperty("序号")
    private Integer index;

    @ApiModelProperty("仪器设备名称")
    private String deviceName;

    @ApiModelProperty("仪器设备型号")
    private String specificationModel;

    @ApiModelProperty("仪器编号")
    private String managementNumber;
}
