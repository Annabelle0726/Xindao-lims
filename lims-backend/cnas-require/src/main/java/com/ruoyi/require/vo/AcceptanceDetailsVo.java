package com.ruoyi.require.vo;

import com.ruoyi.require.pojo.FeStandardSubstance;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptance;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptanceInspection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AcceptanceDetailsVo extends FeStandardSubstanceAcceptance {

    @ApiModelProperty("物质清单")
    private FeStandardSubstance substance;

    @ApiModelProperty("验收单")
    private FeStandardSubstanceAcceptance acceptance;

    @ApiModelProperty("开箱记录")
    private List<FeStandardSubstanceAcceptanceInspection> list;
}
