package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/10/16
 */
@Data
public class DataAnalysisDto {

    @ApiModelProperty("时间类型, 1:本周, 2:本月, 3:本年")
    private String dateType;

    @ApiModelProperty("检测类型, 1:进厂检验, 2:季度检验")
    private String orderType;

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("型号")
    private String modelName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("分组类型, 0: 默认按照样品区分 1: 同一厂家, 同一型号, 不同批次, 2 : 同一型号, 不同厂家")
    private String groupType;

    @ApiModelProperty("选择的检验项名称")
    private List<String> itemNames;

    @ApiModelProperty("订单id")
    private List<Integer> orderIds;

    @ApiModelProperty("厂家数据")
    private List<String> supplierDataList;

}
