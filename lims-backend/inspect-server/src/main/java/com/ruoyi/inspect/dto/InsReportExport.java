package com.ruoyi.inspect.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/4/18
 */
@Data
@ExcelIgnoreUnannotated
public class InsReportExport {

    @ExcelProperty(value = "报告编号")
    @ApiModelProperty("委托编号")
    private String code;

    @ExcelProperty(value = "下单类型")
    @ApiModelProperty("下单类型")
    private String typeSource;

    @ExcelProperty(value = "检验类型")
    @ApiModelProperty("检验类型")
    private String orderType;

    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private String createTime;

    @ExcelProperty(value = "提交人")
    @ApiModelProperty("提交人")
    private String writeUserName;

    @ExcelProperty(value = "提交时间")
    @ApiModelProperty("提交时间")
    private String writeTime;

    @ExcelProperty(value = "提交状态")
    @ApiModelProperty("提交状态")
    private String state;

    @ExcelProperty(value = "审核人")
    @ApiModelProperty("审核人")
    private String examineUser;

    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private String examineTime;

    @ExcelProperty(value = "审核状态")
    @ApiModelProperty("审核状态")
    private String isExamine;

    @ExcelProperty(value = "审核备注")
    @ApiModelProperty("审核备注")
    private String examineTell;

    @ExcelProperty(value = "批准人")
    @ApiModelProperty("批准人")
    private String ratifyUser;

    @ExcelProperty(value = "批准时间")
    @ApiModelProperty("批准时间")
    private String ratifyTime;

    @ExcelProperty(value = "批准状态")
    @ApiModelProperty("批准状态")
    private String isRatify;

    @ExcelProperty(value = "批准备注")
    @ApiModelProperty("批准备注")
    private String ratifyTell;
}
