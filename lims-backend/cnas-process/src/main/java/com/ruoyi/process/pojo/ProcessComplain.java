package com.ruoyi.process.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 投诉
 * </p>
 *
 * @author
 * @since 2024-11-02 09:29:11
 */
@Getter
@Setter
@TableName("cnas_process_complain")
@ApiModel(value = "ProcessComplain对象", description = "投诉")
public class ProcessComplain  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @ApiModelProperty("投诉编号")
    @ExcelIgnore
    private String complainNo;

    @ApiModelProperty("投诉名称")
    @ExcelProperty(value = "投诉名称")
    private String complainName;

    @ApiModelProperty("外键关联(检测报告id)")
    @ExcelIgnore
    private Integer insReportId;

    @ExcelIgnore
    @TableField(select = false,exist = false)
    //用来新增传参的报告编号
    private String code;

    @ApiModelProperty("样品编号")
    @ExcelIgnore
    private String sampleCode;

    @ApiModelProperty("投诉人")
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private Integer createUser;

    @ExcelProperty(value = "投诉人")
    @TableField(select = false,exist = false)
    private String complainant;

    @ApiModelProperty("投诉日期")
    @ExcelProperty(value = "投诉日期")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private LocalDateTime updateTime;

    @ApiModelProperty("投诉方式")
    @ExcelIgnore
    private String complainMethod;

    @ApiModelProperty("问题记录")
    @ExcelIgnore
    private String problemRecords;

    @ApiModelProperty("问题记录-质量负责人")
    @ExcelIgnore
    private Integer problemRecordsUser;

    @ApiModelProperty("问题记录-日期")
    @ExcelIgnore
    private LocalDate problemRecordsTime;

    @ApiModelProperty("责任归属及投诉是否成立")
    @ExcelIgnore
    private String dutyOwnership;

    @ApiModelProperty("责任归属及投诉是否成立_质量负责人")
    @ExcelIgnore
    private Integer dutyOwnershipUser;

    @ApiModelProperty("责任归属及投诉是否成立_日期")
    @ExcelIgnore
    private LocalDate dutyOwnershipTime;

    @ApiModelProperty("原因分析")
    @ExcelIgnore
    private String causeAnalysis;

    @ApiModelProperty("原因分析_责任部门负责人")
    @ExcelIgnore
    private Integer causeAnalysisUser;

    @ApiModelProperty("原因分析_日期")
    @ExcelIgnore
    private LocalDate causeAnalysisTime;

    @ApiModelProperty("纠正措施")
    @ExcelIgnore
    private String correctiveAction;

    @ApiModelProperty("纠正措施_责任部门负责人")
    @ExcelIgnore
    private Integer correctiveActionUser;

    @ApiModelProperty("纠正措施_日期")
    @ExcelIgnore
    private LocalDate correctiveActionTime;

    @ApiModelProperty("纠正措施确认")
    @ExcelIgnore
    private String correctiveActionConfirmation;

    @ApiModelProperty("纠正措施确认_质量负责人")
    @ExcelIgnore
    private Integer correctiveActionConfirmationUser;

    @ApiModelProperty("纠正措施确认_日期")
    @ExcelIgnore
    private LocalDate correctiveActionConfirmationTime;
}
