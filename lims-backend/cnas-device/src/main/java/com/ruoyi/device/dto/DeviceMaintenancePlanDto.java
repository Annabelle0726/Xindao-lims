package com.ruoyi.device.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.device.pojo.DeviceMaintenancePlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: yuan
 * Date: 2024-12-16 星期一 18:26:59
 * Description:
 */
@Data
public class DeviceMaintenancePlanDto extends DeviceMaintenancePlan {

    @ApiModelProperty("设备保养计划详情")
    @TableField(exist = false)
    private List<DeviceMaintenancePlanDetailsDto> deviceMaintenancePlanDetails;

    @ApiModelProperty("编制日期")
    private String datePreparationStr;

    @ApiModelProperty("审核日期")
    private String auditDateStr;

    @ApiModelProperty("设备Id")
    private Integer deviceId;
}
