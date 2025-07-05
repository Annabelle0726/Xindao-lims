package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeIllumination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FeIlluminationDto extends FeIllumination {
    @ApiModelProperty("检测者")
    private String checkerUser;

    @ApiModelProperty("核查人")
    private String testerUser;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;

}
