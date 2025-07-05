package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监控计划详情批准表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Data
@TableName("cnas_quality_monitor_details_ratify")
@ApiModel(value = "QualityMonitorDetailsRatify对象", description = "质量监控计划详情批准表")
public class QualityMonitorDetailsRatify {

    @TableId(type = IdType.AUTO)
    private Integer detailsRatifyId;

    @ApiModelProperty("监控计划详情id")
    private Integer qualityMonitorDetailsId;

    @ApiModelProperty("监控项目")
    private String monitorProject;

    @ApiModelProperty("监控目的")
    private String monitorPurpose;

    @ApiModelProperty("监控时间")
    private String monitorData;

    @ApiModelProperty("监控方法")
    private String monitorMethod;

    @ApiModelProperty("参加人员")
    private String participant;

    @ApiModelProperty("过程控制")
    private String processControl;

    @ApiModelProperty("如何评价")
    private String howEvaluate;

    @ApiModelProperty("预算")
    private String budget;

    @ApiModelProperty("检测部门")
    private String inspectionDepartment;

    @ApiModelProperty("批准意见")
    private String ratifyOpinion;

    @ApiModelProperty("技术负责人")
    private Integer ratifyUserId;

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

    @ApiModelProperty("是否结束0:未结束, 1:已结束")
    private Integer isFinish;

    @ApiModelProperty("技术负责人")
    @TableField(exist = false,select = false)
    private String ratifyName;

}

