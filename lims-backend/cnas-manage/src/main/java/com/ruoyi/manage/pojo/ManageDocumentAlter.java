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
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件变更
 * </p>
 *
 * @author
 * @since 2024-11-11 11:04:01
 */
@Getter
@Setter
@TableName("cnas_manage_document_alter")
@ApiModel(value = "ManageDocumentAlter对象", description = "文件变更")
@ExcelIgnoreUnannotated
public class ManageDocumentAlter  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("申请编号")
    @ExcelProperty("申请编号")
    private String code;

    @ApiModelProperty("申请人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(select = false, exist = false)
    @ExcelProperty("申请人")
    private String createUserName;

    @TableField(select = false, exist = false)
    @ApiModelProperty("申请部门")
    private String createUserDepartLims;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("期望变更时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("期望变更时间")
    private LocalDate expectAlterDate;

    @ApiModelProperty("实际变更时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("实际变更时间")
    private LocalDate actuallyAlterDate;

    @ApiModelProperty("状态")
    @ExcelProperty("状态")
    private String state;

    @ApiModelProperty("审批人")
    private Integer checkUser;

    @TableField(select = false, exist = false)
    @ExcelProperty("审批人")
    private String checkUserName;

    @ApiModelProperty("变更说明")
    @ExcelProperty("变更说明")
    private String alterNote;

    @ApiModelProperty("变更前编号")
    private String alterBeforeCode;

    @ApiModelProperty("变更前名称")
    private String alterBeforeName;

    @ApiModelProperty("变更前版本")
    private String alterBeforeVersion;

    @ApiModelProperty("变更后编号")
    private String alterAfterCode;

    @ApiModelProperty("变更后名称")
    private String alterAfterName;

    @ApiModelProperty("变更后版本")
    private String alterAfterVersion;

    @ApiModelProperty("变更后附件")
    private String alterAfterUrl;

    @ApiModelProperty("变更后附件")
    @TableField(select = false,exist = false)
    private MultipartFile file;

    @ApiModelProperty("前一版本处理方式")
    private String method;
}
