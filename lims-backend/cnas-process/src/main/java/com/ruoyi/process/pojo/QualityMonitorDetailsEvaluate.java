package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监控计划详情评价表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Data
@TableName("cnas_quality_monitor_details_evaluate")
@ApiModel(value = "QualityMonitorDetailsEvaluate对象", description = "质量监控计划详情评价表")
public class QualityMonitorDetailsEvaluate {

    @TableId(type = IdType.AUTO)
    private Integer detailsEvaluateId;

    @ApiModelProperty("监控计划详情id")
    private Integer qualityMonitorDetailsId;

    @ApiModelProperty("评审目的")
    private String reviewPurpose;

    @ApiModelProperty("评审人员")
    private String reviewUser;

    @ApiModelProperty("评审日期")
    private String reviewTime;

    @ApiModelProperty("实施情况")
    private String implementCondition;

    @ApiModelProperty("实施部门")
    private String implementDepartment;

    @ApiModelProperty("实施结果")
    private String implementResult;

    @ApiModelProperty("实施技术负责人")
    private Integer implementUserId;

    @ApiModelProperty("批准结论")
    private String ratifyOpinion;

    @ApiModelProperty("批准技术负责人")
    private Integer ratifyUserId;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime ratifyTime;

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

    @ApiModelProperty("实施技术负责人名称")
    @TableField(exist = false,select = false)
    private String implementName;

    @ApiModelProperty("批准技术负责人名称")
    @TableField(exist = false,select = false)
    private String ratifyUserName;
}

