package com.ruoyi.inspect.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/10/24
 */
@Data
public class RawMaterialSupplierVo {

    @ApiModelProperty("订单id")
    private Integer OrderId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("检测编号")
    private String entrustCode;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("检测值")
    private String lastValue;

}
