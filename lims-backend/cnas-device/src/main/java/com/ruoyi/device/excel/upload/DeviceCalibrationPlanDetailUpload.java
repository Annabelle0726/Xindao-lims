package com.ruoyi.device.excel.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/12/20
 */
@Data
public class DeviceCalibrationPlanDetailUpload {

    @ApiModelProperty("设备名称及型号")
    private String deviceName;

    @ApiModelProperty("设备数量")
    private String deviceAmount;

    @ApiModelProperty("仪器编号")
    private String deviceNumber;

    @ApiModelProperty("检定单位")
    private String verificationUnit;

    @ApiModelProperty("检定周期")
    private String verificationCycles;

    @ApiModelProperty("最近检定时间")
    private String lastDate;

    @ApiModelProperty("本年计划校准时间")
    private String planDate;

    @ApiModelProperty("备注")
    private String remark;
}
