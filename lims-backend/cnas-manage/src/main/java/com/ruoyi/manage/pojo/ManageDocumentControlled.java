package com.ruoyi.manage.pojo;

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
 * 文件受控
 * </p>
 *
 * @author
 * @since 2024-11-08 02:54:44
 */
@Getter
@Setter
@TableName("cnas_manage_document_controlled")
@ApiModel(value = "ManageDocumentControlled对象", description = "文件受控")
public class ManageDocumentControlled  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("申请文件编号")
    private String documentCode;

    @ApiModelProperty("文件类别")
    private String type;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件版本")
    private String version;

    @ApiModelProperty("作者")
    private String writer;

    @ApiModelProperty("提交日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate submitDate;

    @ApiModelProperty("说明")
    private String instructions;

    @ApiModelProperty("申请状态")
    private String state;

    @ApiModelProperty("附件地址")
    private String url;

    @ApiModelProperty("责任人")
    private Integer dutyUser;

    @TableField(select = false,exist = false)
    private String dutyUserName;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(select = false, exist = false)
    private String createUserName;

    @ApiModelProperty("申请部门")
    @TableField(select = false, exist = false)
    private String createUserDepartLims;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(select = false, exist = false)
    private MultipartFile file;
}
