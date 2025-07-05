package com.ruoyi.inspect.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsOrderPlanTaskSwitchVo{
    private String id;

    private String entrustCode;

    private String sampleType;

    @ApiModelProperty("紧急程度")
    private String type;

    @ApiModelProperty("状态")
    private String insState;

    @ApiModelProperty("约定时间")
    private String appointed;

    @ApiModelProperty("下发时间")
    private String sendTime;

    private Integer userId;

    private String sonLaboratory;

    private String laboratory;

    private Integer orderUserId;

    private Integer verifyUser;
}
