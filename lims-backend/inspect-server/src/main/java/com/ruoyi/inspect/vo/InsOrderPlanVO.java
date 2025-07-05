package com.ruoyi.inspect.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsOrderPlanVO{


    private Integer id;

    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("样品型号")
    private String sampleModel;

    @ApiModelProperty("紧急程度")
    private String type;

    @ApiModelProperty("检验类型")
    private String orderType;

    @ApiModelProperty("状态")
    private String insState;

    @ApiModelProperty("检验人")
    private String userName;

    @ApiModelProperty("复核人")
    private String checkName;

    private Integer userId;

    @ApiModelProperty("检验开始时间")
    private String insTime;

    @ApiModelProperty("约定时间")
    private String appointed;

    @ApiModelProperty("下发时间")
    private String sendTime;

    @ApiModelProperty("理由")
    private String verifyTell;


    private String sonLaboratory;

    private Integer orderUserId;

    private Integer verifyUser;

    @ApiModelProperty("下单类别")
    private Integer typeSource;

    @ApiModelProperty("原材料id")
    private Integer ifsInventoryId;

    @ApiModelProperty("报告地址")
    private String url;

    @ApiModelProperty("新地址")
    private String urlS;

    @ApiModelProperty("是否是铜单丝")
    private Integer isCopper;

    @ApiModelProperty("查看临时报告地址")
    private String tempUrlPdf;

    @ApiModelProperty("报告id")
    private Integer insReportId;

}
