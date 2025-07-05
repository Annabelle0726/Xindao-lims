package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InsOrderPlanDTO implements Serializable {

    @ApiModelProperty("检验任务主键id")
    private Long insSampleId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("状态(检验处理)")
    private Integer state;

    @ApiModelProperty("子实验室")
    private String sonLaboratory;

    @ApiModelProperty("检验状态")
    private String insState;

    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("检验类型")
    private Integer typeSource;

    @ApiModelProperty("是否复核")
    private Integer isCheck;

}
