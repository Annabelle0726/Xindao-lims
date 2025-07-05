package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监督详情表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_supervise_details")
@ApiModel(value = "QualitySuperviseDetails对象", description = "质量监督详情表")
public class QualitySuperviseDetails {

    @TableId(type = IdType.AUTO)
    private Integer superviseDetailsId;

    @ApiModelProperty("监督计划id")
    private Integer superviseId;

    @ApiModelProperty("监督日期")
    private String superviseTime;

    @ApiModelProperty("监督目的")
    private String supervisePurpose;

    @ApiModelProperty("监督项目")
    private String superviseProject;

    @ApiModelProperty("被监督人id")
    private Integer supervisedUserId;

    @ApiModelProperty("被监督人")
    private String supervisee;

    @ApiModelProperty("监督原因")
    private String superviseReason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("监督员id")
    private Integer recordUserId;

    @ApiModelProperty("监督员名称")
    private Integer recordUserName;

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

