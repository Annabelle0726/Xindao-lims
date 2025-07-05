package com.ruoyi.process.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 测量不确定度的评价
 * </p>
 *
 * @author
 * @since 2024-11-02 01:10:43
 */
@Getter
@Setter
@TableName("cnas_process_evaluate")
@ApiModel(value = "ProcessEvaluate对象", description = "测量不确定度的评价")
public class ProcessEvaluate  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @ApiModelProperty("报告名称")
    @ExcelProperty(value = "报告名称")
    private String reportName;

    @ApiModelProperty("报告路径")
    @ExcelIgnore
    private String reportUrl;

    @ApiModelProperty("评价人")
    @ExcelIgnore
    private Integer evaluateUser;

    @TableField(select = false,exist = false)
    @ExcelProperty(value = "评价人")
    private String evaluateUserName;

    @ApiModelProperty("评价日期")
    @ExcelProperty(value = "评价日期")
    private LocalDate evaluateTime;

    @ApiModelProperty("备注")
    @ExcelProperty(value = "备注")
    private String note;

    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private LocalDateTime updateTime;
}
