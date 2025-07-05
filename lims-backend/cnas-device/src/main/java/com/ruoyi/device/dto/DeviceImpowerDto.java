package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceImpower;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2025/4/17
 */
@Data
public class DeviceImpowerDto extends DeviceImpower {

    @ApiModelProperty("详情")
    private List<DeviceImpowerDetailsDto> deviceImpowerDetails;

    @ApiModelProperty("授权日期中文")
    private String auditDateCH;

    @ApiModelProperty("授权日期英文")
    private String auditDateEN;
}
