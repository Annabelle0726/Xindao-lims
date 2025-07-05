package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceMaintenancePlanDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 9:26:48
 * Description: 设备保养计划详情
 */
@Data
public class DeviceMaintenancePlanDetailsDto extends DeviceMaintenancePlanDetails {

    @ApiModelProperty("序号")
    private Integer index;

    @ApiModelProperty("仪器设备名称")
    private String deviceName;

    @ApiModelProperty("仪器设备编号")
    private String deviceNumber;

    @ApiModelProperty("仪器设备型号")
    private String specificationModel;

    @ApiModelProperty("仪器编号")
    private String managementNumber;

    @ApiModelProperty("归属实验室")
    private String storagePoint;
}
