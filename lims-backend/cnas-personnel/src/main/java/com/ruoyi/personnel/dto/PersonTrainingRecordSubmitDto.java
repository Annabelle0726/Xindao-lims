package com.ruoyi.personnel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonTrainingRecordSubmitDto {

    @ApiModelProperty("年度计划明细ID")
    private Integer trainingDetailedId;

    @ApiModelProperty("培训地点")
    private String placeTraining;

    @ApiModelProperty("培训完成时间")
    private LocalDate openingTime;

    @ApiModelProperty("考核方式")
    private String assessmentMethod;

    @ApiModelProperty("本次培训综合评价")
    private String comprehensiveAssessment;

    @ApiModelProperty("评价人")
    private Integer assessmentUserId;

    @ApiModelProperty("评价时间")
    private LocalDate assessmentDate;

    private Integer state;


}
