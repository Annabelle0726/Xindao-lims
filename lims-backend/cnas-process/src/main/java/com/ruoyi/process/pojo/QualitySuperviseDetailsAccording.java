package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 质量监督详情不符合项控制表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_supervise_details_according")
@ApiModel(value = "QualitySuperviseDetailsAccording对象", description = "质量监督详情不符合项控制表")
public class QualitySuperviseDetailsAccording {

    @TableId(type = IdType.AUTO)
    private Integer superviseDetailsAccordingId;

    @ApiModelProperty("监督详情id")
    private Integer superviseDetailsId;

    @ApiModelProperty("0发生部门")
    private String occurrenceDepartment;

    @ApiModelProperty("0部门负责人")
    private String headDepartment;

    @ApiModelProperty("0发现途径,0:管理评审,1:内部审核,2:检测过程控制,3:内部质量控制,4:内部监督,5:外部评审,6:外部投诉,7:其他")
    private Integer findWay;

    @ApiModelProperty("0不符合记录详情")
    private String recordDetail;

    @ApiModelProperty("0不合格记录依据")
    private String recordAccording;

    @ApiModelProperty("0发现部门")
    private String foundDepartment;

    @ApiModelProperty("0记录人id")
    private Integer recordUserId;

    @ApiModelProperty("0记录人")
    private String recordUserName;

    @ApiModelProperty("0记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordTime;

    @ApiModelProperty("0被监督人id")
    private Integer supervisedUserId;

    @ApiModelProperty("0被监督人")
    private String supervisedUserName;

    @ApiModelProperty("0被监督时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate supervisedTime;

    @ApiModelProperty("1清除不符合措施")
    private String eliminateMeasure;

    @ApiModelProperty("1责任部门")
    private String responsibleDepartment;

    @ApiModelProperty("1处理人id")
    private Integer actionsUserId;

    @ApiModelProperty("1处理人")
    private String actionsUserName;

    @ApiModelProperty("1处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actionsTime;

    @ApiModelProperty("2纠正措施内容")
    private String correctContent;

    @ApiModelProperty("2是否纠正处理, 0否, 1是")
    private Integer isCorrect;

    @ApiModelProperty("2纠正技术负责人id")
    private Integer correctUserId;

    @ApiModelProperty("2纠正技术负责人")
    private String correctUserName;

    @ApiModelProperty("2纠正填写时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate correctTime;

    @ApiModelProperty("3是否通知客户, 0否, 1是")
    private Integer notifyCustomer;

    @ApiModelProperty("3是否恢复工作, 0否, 1是")
    private Integer backToWork;

    @ApiModelProperty("3质量负责人id")
    private Integer qualityManagerUserId;

    @ApiModelProperty("3质量负责人")
    private String qualityManagerUserName;

    @ApiModelProperty("3质量负责人填写时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate qualityManagerTime;

    @ApiModelProperty("是否结束,0: 未结束, 1:结束")
    private Integer isFinish;

    @ApiModelProperty("批准人")
    private Integer approverUserId;

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

    @TableField(exist = false,select = false)
    @ApiModelProperty("流程, 0:不符合工作情况记录, 1处理措施, 2:纠正措施, 3:是否通知客户可恢复工作")
    private Integer flowType;

}

