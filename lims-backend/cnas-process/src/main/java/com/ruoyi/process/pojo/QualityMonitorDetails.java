package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监控计划详情表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Data
@TableName("cnas_quality_monitor_details")
@ApiModel(value = "QualityMonitorDetails对象", description = "质量监控计划详情表")
public class QualityMonitorDetails {

    @TableId(type = IdType.AUTO)
    private Integer qualityMonitorDetailsId;

    @ApiModelProperty("监控计划id")
    private Integer qualityMonitorId;

    @ApiModelProperty("监控目的")
    private String monitorPurpose;

    @ApiModelProperty("计划开展时间")
    private String plannedTime;

    @ApiModelProperty("监控项目")
    private String monitorProject;

    @ApiModelProperty("参加人员")
    private String participant;

    @ApiModelProperty("预算")
    private String budget;

    @ApiModelProperty("组织人员")
    private String organization;

    @ApiModelProperty("监控方式")
    private String monitorWay;

    @ApiModelProperty("完成报告地址")
    private String finishReportUrl;

    @ApiModelProperty("完成报告编写时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;


    @ApiModelProperty("完成报告批准人")
    private Integer ratifyUserId;

    @ApiModelProperty("完成报告批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("完成报告批准状态, 0 不通过, 1通过")
    private Integer ratifyStatus;

    @ApiModelProperty("完成报告批准内容")
    private String ratifyRemark;

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

    // 导出使用
    @TableField(select = false, exist = false)
    private Integer index;

}

