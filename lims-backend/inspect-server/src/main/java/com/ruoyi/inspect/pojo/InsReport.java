package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验报告
 * @TableName ins_report
 */
@TableName(value ="ins_report")
@Data
public class InsReport implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("报告编号")
    private String code;

    /**
     * 外键：检验单id
     */
    @ApiModelProperty("创建时间")
    private Integer insOrderId;

    /**
     * 系统生成报告地址
     */
    @ApiModelProperty("创建时间")
    private String url;

    /**
     * 手动上传报告地址
     */
    @ApiModelProperty("创建时间")
    private String urlS;

    /**
     * 临时报告地址
     */
    @ApiModelProperty("创建时间")
    private String tempUrlPdf;

    /**
     * 1：批准 0：不批准
     */
    @ApiModelProperty("批准状态")
    private Integer isRatify;

    @ApiModelProperty("批准备注")
    private String ratifyTell;

    /**
     * 1：审核通过 0：审核不通过
     */
    @ApiModelProperty("审核状态")
    private Integer isExamine;

    @ApiModelProperty("审核备注")
    private String examineTell;

    /**
     * 外键：用户id（提交人）
     */
    @ApiModelProperty("创建时间")
    private Integer writeUserId;

    /**
     * 外键：用户id（批准人）
     */
    @ApiModelProperty("创建时间")
    private Integer ratifyUserId;

    /**
     * 外键：用户id（审核人）
     */
    @ApiModelProperty("创建时间")
    private Integer examineUserId;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 1：已提交 0：待提交
     */
    @ApiModelProperty("提交状态")
    private Integer state;

    @ApiModelProperty("提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;

    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examineTime;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratifyTime;


    @ApiModelProperty(value = "是否是通过的报告, 0:未通过, 1:通过")
    private Integer isPass;

}
