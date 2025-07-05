package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeTempHumDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FeTempHumDateDto extends FeTempHumDate {

    @ApiModelProperty("创建人")
    private String createName;

    @ApiModelProperty("记录员")
    private String registrarUserName;

    @ApiModelProperty("温度区间")
    private String temperatureSection;

    @ApiModelProperty("湿度区间")
    private String humiditySection;
}
