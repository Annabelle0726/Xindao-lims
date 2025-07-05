package com.ruoyi.personnel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrainingRecordPersonDetailedDto {

    @ApiModelProperty("培训日期")
    private String trainingDateString;

    @ApiModelProperty("培训日期")
    private String trainingDate;

    @ApiModelProperty("培训内容")
    private String trainingContent;

    @ApiModelProperty("课时")
    private Integer classHour;

    @ApiModelProperty("考核结果")
    private String examinationResults;

    @ApiModelProperty("备注")
    private String remarks;
}
