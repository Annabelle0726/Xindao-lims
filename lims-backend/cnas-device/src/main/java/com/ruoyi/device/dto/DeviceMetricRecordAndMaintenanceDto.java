package com.ruoyi.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-13 星期五 10:43:06
 * Description: 仪器设备档案卡中显示的设备校准记录和维护记录的列表对象
 */
@Data
public class DeviceMetricRecordAndMaintenanceDto {
    @ApiModelProperty("序号")
    private Integer index;

    // 校准表中的数据
    @ApiModelProperty("校准日期")
    private String calibrationDateString;

    @ApiModelProperty("证书标号")
    private String certificateNumber;

    @ApiModelProperty("校准有效日期")
    private String validityDateString;

    @ApiModelProperty("判定")
    private String judgement;

    // 维修记录表中的数据
    @ApiModelProperty("维修日期")
    private String maintenanceDateString;

    @ApiModelProperty("处理方式")
    private String handlingMethod;

    @ApiModelProperty("备注")
    private String comments;
}
