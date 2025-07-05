package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceTraceabilityManagement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: yuan
 * Date: 2024-12-20 星期五 14:30:45
 * Description:
 */
@Data
public class DeviceTraceabilityManagementDto extends DeviceTraceabilityManagement {

    @ApiModelProperty("设备量值溯源计划详情")
    private List<DeviceTraceabilityManagementDetailsDto> deviceTraceabilityManagementDetails;

    @ApiModelProperty("编制日期")
    private String datePreparationStr;

    @ApiModelProperty("审核日期")
    private String auditDateStr;

    @ApiModelProperty("设备Id")
    private Integer deviceId;
}
