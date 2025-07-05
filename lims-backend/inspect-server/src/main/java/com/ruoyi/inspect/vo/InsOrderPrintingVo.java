package com.ruoyi.inspect.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author zhuo
 * @Date 2024/12/3
 */
@Data
public class InsOrderPrintingVo {

    @ApiModelProperty("订单编号")
    private Integer insOrderId;

    @ApiModelProperty("样品名称")
    private String sampleView;

    @ApiModelProperty("生产单位")
    private String production;

    @ApiModelProperty("规格型号")
    private String sampleModel;

    @ApiModelProperty("委托日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty("委托人")
    private String prepareUser;

    @ApiModelProperty("检测编号")
    private String entrustCode;

    @ApiModelProperty("样品数量")
    private String testQuantity;

    @ApiModelProperty("条形码")
    private String labelBarCode;

    @ApiModelProperty("订单状态")
    private Integer insState;

}
