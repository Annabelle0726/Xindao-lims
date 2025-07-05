package com.ruoyi.personnel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonTrainingRecordListDto {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty("员工编号")
    private String account;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("所在部门")
    private String departLimsName;

    @ApiModelProperty("职称")
    private String professionalTitle;

    @ApiModelProperty("最高学历")
    private String officialAcademicRedentials;

    @ApiModelProperty("入单位时间")
    private LocalDateTime unitTime;

    @ApiModelProperty("入单位时间")
    private String unitTimeSting;

    @ApiModelProperty("专业")
    private String major1;
}
