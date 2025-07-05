package com.ruoyi.performance.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
//原始工时统计的查询条件
public class AuxiliaryOriginalHoursLookDto {

    @NotNull
    private String month;//月份

    private String name;

    private String departLims;


    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;


    @ApiModelProperty("辅助工时开始时间")
    private String assistBeginDate;

    @ApiModelProperty("辅助工时结束时间")
    private String assistEndDate;
}
