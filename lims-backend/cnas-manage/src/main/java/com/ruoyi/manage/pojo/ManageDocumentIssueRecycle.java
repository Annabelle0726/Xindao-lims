package com.ruoyi.manage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件发放回收
 * </p>
 *
 * @author
 * @since 2024-11-09 09:18:24
 */
@Getter
@Setter
@TableName("cnas_manage_document_issue_recycle")
@ApiModel(value = "ManageDocumentIssueRecycle对象", description = "文件发放回收")
public class ManageDocumentIssueRecycle  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件编号")
    @ExcelProperty(value = "文件编号")
    private String documentCode;

    @ApiModelProperty("文件名称")
    @ExcelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty("文件版本")
    @ExcelProperty(value = "文件版本")
    private String version;

    @ApiModelProperty("状态")
    @ExcelProperty(value = "状态")
    private String state;

    @ApiModelProperty("文件状态")
    @ExcelProperty(value = "文件状态")
    private String documentState;

    @ApiModelProperty("发放编号")
    @ExcelProperty(value = "发放编号")
    private String issueCode;

    @ApiModelProperty("发放人")
    private Integer issueUser;

    @ApiModelProperty("发放日期")
    @ExcelProperty(value = "发放日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @ApiModelProperty("发放说明")
    private String issueNote;

    @ApiModelProperty("接收人")
    private Integer receiveUser;

    @ApiModelProperty("接收时间")
    private LocalDate receiveDate;

    @ApiModelProperty("回收编号")
    private String recycleCode;

    @ApiModelProperty("回收人")
    private Integer recycleUser;

    @ApiModelProperty("回收日期")
    @ExcelProperty(value = "回收日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recycleDate;

    @ApiModelProperty("回收说明")
    private String recycleNote;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("附件")
    private String url;

    @TableField(select = false,exist = false)
    private MultipartFile file;
}
