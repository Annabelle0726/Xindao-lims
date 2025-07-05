package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监控计划主表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Data
@TableName("cnas_quality_monitor")
@ApiModel(value = "QualityMonitor对象", description = "质量监控计划主表")
public class QualityMonitor {

    @TableId(type = IdType.AUTO)
    private Integer qualityMonitorId;

    @ApiModelProperty("计划名称")
    private String monitorName;

    @ApiModelProperty("计划年份")
    private String monitorYear;

    @ApiModelProperty("外键：用户id（编制人）")
    private Integer writeUserId;

    @ApiModelProperty("编制时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;

    @ApiModelProperty("外键：用户id（审核人）")
    private Integer examineUserId;

    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examineTime;

    @ApiModelProperty("审核状态 , 0 不通过, 1通过")
    private Integer examineStatus;

    @ApiModelProperty("审核内容")
    private String examineRemark;

    @ApiModelProperty("外键：用户id（批准人）")
    private Integer ratifyUserId;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态, 0 不通过, 1通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准内容")
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

}

