package com.ruoyi.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IfsInventoryQuantityDto extends IfsInventoryQuantityCheckDto {

    /**
     * 委托编号
     */
    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("检验对象")
    private String sampleType;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("样品型号")
    private String sampleModel;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    // 进厂
    @ApiModelProperty("订单id")
    private Integer enterOrderId;

    @ApiModelProperty("报告id")
    private String enterReportId;

    @ApiModelProperty("系统生成报告地址")
    private String enterUrl;

    @ApiModelProperty("手动上传报告地址")
    private String enterUrlS;

    // 季度
    @ApiModelProperty("订单id")
    private Integer quarterOrderId;

    @ApiModelProperty("报告id")
    private String quarterReportId;

    @ApiModelProperty("系统生成报告地址")
    private String quarterUrl;

    @ApiModelProperty("手动上传报告地址")
    private String quarterUrlS;

    private Integer orderState;

    @ApiModelProperty("下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty("材料厂家")
    private String supplierName;

    @ApiModelProperty("委托人")
    private String prepareUser;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("标签状态")
    private String labelStatus;

    @ApiModelProperty("标签条形码")
    private String labelBarCode;

    @ApiModelProperty("创建人")
    private Integer createUser;

    @ApiModelProperty("检验人")
    private String userName;
}
