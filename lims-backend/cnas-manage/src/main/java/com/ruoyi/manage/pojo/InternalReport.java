package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内审报告表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Data
@TableName("cnas_internal_report")
public class InternalReport {

    @TableId(type = IdType.AUTO)
    private Integer reportId;

    @ApiModelProperty("审核依据")
    private String purposes;

    @ApiModelProperty("审核依据")
    private String basis;

    @ApiModelProperty("审核日期")
    private String reviewDate;

    @ApiModelProperty("审核方法")
    private String method;

    @ApiModelProperty("审核范围")
    private String scope;

    @ApiModelProperty("审核责任制")
    private String responsible;

    @ApiModelProperty("审核组长")
    private String leader;

    @ApiModelProperty("审核员")
    private String auditor;

    @ApiModelProperty("审核组分工情况")
    private String division;

    @ApiModelProperty("审核概况")
    private String overview;

    @ApiModelProperty("结论性评价")
    private String conclusion;

    @ApiModelProperty("改进建议")
    private String suggest;

    @ApiModelProperty("完成纠正措施所需时间")
    private String actionDate;

    @ApiModelProperty("总体跟进确认人")
    private String followUser;

    @ApiModelProperty("总体跟进确认记录")
    private String followRecord;

    @ApiModelProperty("本报告发放范围")
    private String reportScope;

    @ApiModelProperty("审核人id")
    private Integer examineUserId;

    @ApiModelProperty("审核人")
    private String examineUserName;

    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examineTime;

    @ApiModelProperty("审核状态,0 不通过, 1 通过")
    private Integer examineStatus;

    @ApiModelProperty("审核信息")
    private String examineRemark;

    @ApiModelProperty("质量负责人id")
    private Integer qualityUserId;

    @ApiModelProperty("质量负责人")
    private String qualityUserName;

    @ApiModelProperty("质量负责人填写时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime qualityTime;

    @ApiModelProperty("质量负责人状态, 0 不通过, 1通过")
    private Integer qualityStatus;

    @ApiModelProperty("质量负责人意见")
    private String qualityRemark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

