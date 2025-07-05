package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonPersonnelCapacityDto extends PersonPersonnelCapacity {

    @ApiModelProperty("操作人姓名")
    private String confirmOperatingPersonnelName;

    @ApiModelProperty("人员姓名")
    private String userName;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("岗位职责")
    private String responsibilities;

    @ApiModelProperty("工作经历")
    private String placeWork;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty(value = "职称")
    private String professionalTitle;
}
