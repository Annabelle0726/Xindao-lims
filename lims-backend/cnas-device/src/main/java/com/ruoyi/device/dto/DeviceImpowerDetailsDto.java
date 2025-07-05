package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceImpowerDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/4/17
 */
@Data
public class DeviceImpowerDetailsDto extends DeviceImpowerDetails {

    @ApiModelProperty("序号")
    private Integer index;

    @ApiModelProperty("仪器设备名称")
    private String deviceName;

    @ApiModelProperty("仪器设备型号")
    private String specificationModel;

    @ApiModelProperty("仪器编号")
    private String managementNumber;
}
