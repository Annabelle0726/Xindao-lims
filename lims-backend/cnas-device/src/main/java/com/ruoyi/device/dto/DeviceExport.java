package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.Device;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-10 星期二 15:55:29
 * Description:
 */
@Data
public class DeviceExport extends Device {
    @ApiModelProperty("管理人")
    private String equipmentManagerName;

    @ApiModelProperty("序号")
    private Integer index;
}
