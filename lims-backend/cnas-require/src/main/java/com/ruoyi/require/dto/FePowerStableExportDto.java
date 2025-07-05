package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FePowerStable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/28
 */
@Data
public class FePowerStableExportDto extends FePowerStable {

    @ApiModelProperty("测试日期")
    private String testDateString;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;

    @ApiModelProperty("校准日期")
    private String calibrationDateString;

    @ApiModelProperty("下次校准日期")
    private String nextCalibrationDateString;


}
