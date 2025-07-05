package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FePowerStable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FePowerStableDto extends FePowerStable {

    @ApiModelProperty("检测者")
    private String checkerUser;

    @ApiModelProperty("核查人")
    private String testerUser;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;

}
