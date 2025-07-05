package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceCalibrationPlanDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 10:05:08
 * Description:
 */
@Data
public class DeviceCalibrationPlanDetailDto extends DeviceCalibrationPlanDetail {

    @ApiModelProperty("序号")
    private Integer index;
    @ApiModelProperty("最近检定时间Str")
    private String lastDateStr;
    @ApiModelProperty("本年计划校准时间Str")
    private String planDateStr;
}
