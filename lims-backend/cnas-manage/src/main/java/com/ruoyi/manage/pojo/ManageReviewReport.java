package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理评审报告
 * </p>
 *
 * @author 
 * @since 2024-11-12 04:44:39
 */
@Getter
@Setter
@TableName("cnas_manage_review_report")
@ApiModel(value = "ManageReviewReport对象", description = "管理评审报告")
public class ManageReviewReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("目的")
    private String objective;

    @ApiModelProperty("地点")
    private String place;

    @ApiModelProperty("主持人")
    private String compere;

    @ApiModelProperty("记录人")
    private String recordPeople;

    @ApiModelProperty("日期")
    private LocalDateTime date;

    @ApiModelProperty("页次")
    private Integer page;

    @ApiModelProperty("评审方式")
    private String judgingMethod;

    @ApiModelProperty("评审依据")
    private String reviewBasis;

    @ApiModelProperty("出席人员")
    private String attendess;

    @ApiModelProperty("评审输入情况")
    private String reviewInputs;

    @ApiModelProperty("评审过程概况")
    private String reviewProcess;

    @ApiModelProperty("主要议题概述")
    private String mainTopic;

    @ApiModelProperty("事项")
    private String matters;

    @ApiModelProperty("负责人")
    private String head;

    @ApiModelProperty("完成日期")
    private LocalDateTime completionDate;

    @ApiModelProperty("跟踪确认人")
    private String trackingConfirmed;

    @ApiModelProperty("跟踪情况确认记录")
    private String follerUp;

    @ApiModelProperty("体系评价")
    private String overallEvaluation;

    @ApiModelProperty("审核")
    private String audit;

    @ApiModelProperty("批准")
    private String approval;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
