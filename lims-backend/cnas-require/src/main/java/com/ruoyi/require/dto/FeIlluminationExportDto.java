package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeIllumination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/28
 */
@Data
public class FeIlluminationExportDto extends FeIllumination {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;

    @ApiModelProperty("校准日期")
    private String calibrationDateString;

    @ApiModelProperty("下次校准日期")
    private String nextCalibrationDateString;
}
