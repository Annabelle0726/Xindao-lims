package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存模板传参
 */
@Data
public class SaveInsContextDto {

    @ApiModelProperty("模板内容")
    private String param;

    @ApiModelProperty("当前模板id")
    private Integer currentTable;

    @ApiModelProperty("当前样品id")
    private Integer sampleId;

    @ApiModelProperty("当前订单id")
    private Integer orderId;

    @ApiModelProperty("子试验室")
    private String sonLaboratory;

    @ApiModelProperty("是否没有检验值")
    private Integer isNoTestValue;
}
