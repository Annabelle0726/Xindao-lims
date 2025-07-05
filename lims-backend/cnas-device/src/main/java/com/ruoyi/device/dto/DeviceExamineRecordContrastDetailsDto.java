package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceExamineRecordContrastDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 13:59:37
 * Description:
 */
@Data
public class DeviceExamineRecordContrastDetailsDto extends DeviceExamineRecordContrastDetails {
    @ApiModelProperty("序号")
    private Integer index;
}
