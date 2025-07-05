package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监督详情记录表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_supervise_details_record")
@ApiModel(value = "QualitySuperviseDetailsRecord对象", description = "质量监督详情记录表")
public class QualitySuperviseDetailsRecord {

    @TableId(type = IdType.AUTO)
    private Integer superviseDetailsRecordId;

    @ApiModelProperty("监督详情id")
    private Integer superviseDetailsId;

    @ApiModelProperty("检测人员")
    private String testMember;

    @ApiModelProperty("监督员")
    private String supervisor;

    @ApiModelProperty("检测项目")
    private String testItem;

    @ApiModelProperty("样品编号")
    private String sampleNumber;

    @ApiModelProperty("检测日期")
    private String testDate;

    @ApiModelProperty("检测过程")
    private String testingProcess;

    @ApiModelProperty("情况记录")
    private String caseReload;

    @ApiModelProperty("人员")
    private String personnel;

    @ApiModelProperty("仪器设备")
    private String device;

    @ApiModelProperty("环境")
    private String environment;

    @ApiModelProperty("样品采集")
    private String sampleCollection;

    @ApiModelProperty("样品准备")
    private String samplePreparation;

    @ApiModelProperty("检测方法")
    private String detectionMethod;

    @ApiModelProperty("检测记录")
    private String inspectionRecord;

    @ApiModelProperty("检测报告")
    private String examiningReport;

    @ApiModelProperty("监督情况评价")
    private String supervisionEvaluation;

    @ApiModelProperty("不合格处理意见")
    private String handlingAdvice;

    @ApiModelProperty("批准结论")
    private String ratifyOpinion;

    @ApiModelProperty("批准技术负责人")
    private Integer ratifyUserId;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("是否符合,0 不符合, 1符合")
    private Integer isAccording;

    @ApiModelProperty("是否结束0:未结束, 1:已结束")
    private Integer isFinish;

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

    @ApiModelProperty("批准技术负责人")
    @TableField(exist = false,select = false)
    private String ratifyUserName;

}

