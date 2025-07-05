package com.ruoyi.basic.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 能查询到供应商
 */
@Data
@ExcelIgnoreUnannotated
public class IfsInventoryQuantitySupplierDto extends IfsInventoryQuantity {

    /**
     * 委托编号
     */
    @ExcelProperty(index = 2, value = "委托编号")
    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("样品id")
    private Integer sampleId;

    @ApiModelProperty("检验对象")
    private String sampleType;

    @ExcelProperty(index = 7, value = "样品名称")
    @ApiModelProperty("样品名称")
    private String sampleName;

    @ExcelProperty(index = 8, value = "样品型号")
    @ApiModelProperty("样品型号")
    private String sampleModel;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    // 进厂
    @ApiModelProperty("进厂订单id")
    private Integer enterOrderId;

    @ApiModelProperty("报告id")
    private String enterReportId;

    @ApiModelProperty("系统生成报告地址")
    private String enterUrl;

    @ApiModelProperty("手动上传报告地址")
    private String enterUrlS;

    // 季度
    @ApiModelProperty("季度订单id")
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

    @ExcelProperty(index = 9, value = "检验人")
    @ApiModelProperty("检验人")
    private String userName;

    @ExcelProperty(index = 10, value = "下发时间")
    private String sendTimeString;

    @ExcelProperty(index = 14, value = "接收时间")
    private String receiverDateString;

    @ExcelProperty(index = 15, value = "报检时间")
    private String declareDateString;

    // 合格状态,: 0 检验中, 1合格, 2不合格, 3未下单,4让步放行
    @ExcelProperty(index = 6, value = "检验状态")
    private String inspectStatusString;

    @ApiModelProperty("报检开始时间")
    private String beginDeclareDate;

    @ApiModelProperty("报检结束时间")
    private String endDeclareDate;

    @ApiModelProperty("不合格描述")
    @ExcelProperty(index = 16, value = "不合格描述")
    private String unqualifiedDesc;


    @ApiModelProperty("不合格项")
    @ExcelProperty(index = 17, value = "不合格项")
    private String unqualifiedItem;

    @ApiModelProperty("免检")
    private Integer isExemption;

    @ApiModelProperty("原材料id(导出用)")
    private String ids;
}
