package com.ruoyi.manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageMeetingParticipantsDto {

    @ApiModelProperty("参加人员1")
    private String userName1;

    @ApiModelProperty("部门1")
    private String department1;

    @ApiModelProperty("参加人员2")
    private String userName2;

    @ApiModelProperty("部门2")
    private String department2;

}
