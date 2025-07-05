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
 * 文件修订申请审批记录
 * </p>
 *
 * @author
 * @since 2024-11-14 10:29:18
 */
@Getter
@Setter
@TableName("cnas_manage_record_audit")
@ApiModel(value = "ManageRecordAudit对象", description = "文件修订申请审批记录")
public class ManageRecordAudit  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件编号")
    private String documentCode;

    @ApiModelProperty("文件名称")
    private String documentName;

    @ApiModelProperty("章节号")
    private String capter;

    @ApiModelProperty("页码")
    private String pages;

    @ApiModelProperty("修订次数")
    private Integer number;

    @ApiModelProperty("修改前版本号")
    private String beforeVersion;

    @ApiModelProperty("修改后版本号")
    private String afterVersion;

    @ApiModelProperty("修改内容")
    private String alterThing;

    @ApiModelProperty("修订人")
    private Integer alterUser;

    @TableField(select = false, exist = false)
    private String alterUserName;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @TableField(select = false, exist = false)
    private String ratifyUserName;

    //批准人签名
    @TableField(select = false, exist = false)
    private String ratifyUserUrl;

    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty("申请人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    //申请人
    @TableField(select = false, exist = false)
    private String createUserName;

    //申请部门
    @TableField(select = false, exist = false)
    private String createUserDepart;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("变化原因")
    private String reason;

    @ApiModelProperty("原分发部门")
    private String beforeDepart;

    @ApiModelProperty("修订后分发部门")
    private String afterDepart;

    @ApiModelProperty("申请部门主管意见")
    private String applicant;

    @ApiModelProperty("原制定部门意见")
    private String formulation;

    @ApiModelProperty("原审核部门意见")
    private String audit;

    @ApiModelProperty("修订作废")
    private String method;


    @ApiModelProperty("原附件")
    private String beforeUrl;

    @ApiModelProperty("修订后附件")
    private String afterUrl;

    @TableField(select = false, exist = false)
    private MultipartFile file;

}
