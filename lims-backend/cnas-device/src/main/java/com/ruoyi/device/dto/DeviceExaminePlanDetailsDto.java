package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 15:34:44
 * Description:
 */
@Data
public class DeviceExaminePlanDetailsDto extends DeviceExaminePlanDetails {
    @ApiModelProperty("序号")
    private Integer index;

    @ApiModelProperty("记录状态, 0: 未开始, 1:待批准, 2:通过, 3:不通过")
    private Integer recordStatus;

    @ApiModelProperty("对比状态, 0: 未开始, 1:待批准, 2:通过, 3:不通过")
    private Integer recordContrastStatus;

}
