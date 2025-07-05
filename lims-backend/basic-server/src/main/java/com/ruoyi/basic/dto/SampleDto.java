package com.ruoyi.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SampleDto {

    private String code = "[4]";

    private String label;

    private String value;

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("样品英文")
    private String sampleEn;

    @ApiModelProperty("产品")
    private List<ModelDto> children;

}
