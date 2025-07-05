package com.ruoyi.manage.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件作废
 * </p>
 *
 * @author
 * @since 2024-11-09 02:37:35
 */
@Getter
@Setter
@TableName("cnas_manage_document_cancel")
@ApiModel(value = "ManageDocumentCancel对象", description = "文件作废")
@ExcelIgnoreUnannotated
public class ManageDocumentCancel  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("申请人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("申请人")
    @TableField(select = false,exist = false)
    @ExcelProperty("申请人")
    private String createUserName;

    @ApiModelProperty("申请人")
    @TableField(select = false,exist = false)
    private String createUserDepartLims;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("文件编号")

    @ExcelProperty("文件编号")
    private String documentCode;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件版本")
    private String version;

    @ApiModelProperty("文件状态")
    private String documentState;

    @ApiModelProperty("作废方式")
    private String method;

    @ApiModelProperty("作废说明")
    @ExcelProperty("作废说明")
    private String cancelNote;

    @ApiModelProperty("审批人")
    private Integer checkUser;

    @ExcelProperty("审批人")
    @TableField(select = false, exist = false)
    private String checkUserName;

    @ApiModelProperty("期望作废时间")
    @ExcelProperty("期望作废日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectCancelDate;

    @ApiModelProperty("实际作废日期")
    @ExcelProperty("实际作废日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actuallyCancelDate;

    @ApiModelProperty("作废状态")
    @ExcelProperty("作废状态")
    private String state;
}
