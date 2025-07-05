package com.ruoyi.manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 参加人员对象
 *
 * @Author zhuo
 * @Date 2024/11/19
 */
@Data
public class InternalMeetingParticipantDto {

    @ApiModelProperty("参加人员1")
    private String userName1;

    @ApiModelProperty("部门1")
    private String department1;

    @ApiModelProperty("参加人员2")
    private String userName2;

    @ApiModelProperty("部门2")
    private String department2;

    @ApiModelProperty("参加人员3")
    private String userName3;

    @ApiModelProperty("部门3")
    private String department3;
}
