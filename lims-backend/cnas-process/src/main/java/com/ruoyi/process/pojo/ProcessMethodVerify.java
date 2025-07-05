package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标准方法验证
 *
 * @author zhuo
 * @since 2024-11-05
 */
@Data
@TableName("cnas_process_method_verify")
public class ProcessMethodVerify {

    @TableId(type = IdType.AUTO)
    private Integer methodVerifyId;

    @ApiModelProperty("0变更,1验证")
    private Integer operationType;

    @ApiModelProperty("标准方法")
    private String methodName;

    @ApiModelProperty("验证原因")
    private String verifyReason;

    @ApiModelProperty("主要技术变化")
    private String technologyChange;

    @ApiModelProperty("(人)标准要求")
    private String personRequirements;
    @ApiModelProperty("(人)准备情况")
    private String personReadiness;
    @ApiModelProperty("(人)是否满足")
    private Integer personIsSatisfied;
    @ApiModelProperty("(人)备注")
    private String personRemark;

    @ApiModelProperty("(机)标准要求")
    private String machineRequirements;
    @ApiModelProperty("(机)准备情况")
    private String machineReadiness;
    @ApiModelProperty("(机)是否满足")
    private Integer machineIsSatisfied;
    @ApiModelProperty("(机)备注")
    private String machineRemark;

    @ApiModelProperty("(料)标准要求")
    private String materialRequirements;
    @ApiModelProperty("(料)准备情况")
    private String materialReadiness;
    @ApiModelProperty("(料)是否满足")
    private Integer materialIsSatisfied;
    @ApiModelProperty("(料)备注")
    private String materialRemark;

    @ApiModelProperty("(法)标准要求")
    private String methodRequirements;
    @ApiModelProperty("(法)准备情况")
    private String methodReadiness;
    @ApiModelProperty("(法)是否满足")
    private Integer methodIsSatisfied;
    @ApiModelProperty("(法)备注")
    private String methodRemark;

    @ApiModelProperty("(环)标准要求")
    private String environmentRequirements;
    @ApiModelProperty("(环)准备情况")
    private String environmentReadiness;
    @ApiModelProperty("(环)是否满足")
    private Integer environmentIsSatisfied;
    @ApiModelProperty("(环)备注")
    private String environmentRemark;

    @ApiModelProperty("(测量溯源性)标准要求")
    private String traceabilityRequirements;
    @ApiModelProperty("(测量溯源性)准备情况")
    private String traceabilityReadiness;
    @ApiModelProperty("(测量溯源性)是否满足")
    private Integer traceabilityIsSatisfied;
    @ApiModelProperty("(测量溯源性)备注")
    private String traceabilityRemark;

    @ApiModelProperty("(样品管理需求)标准要求")
    private String managementRequirements;
    @ApiModelProperty("(样品管理需求)准备情况")
    private String managementReadiness;
    @ApiModelProperty("(样品管理需求)是否满足")
    private Integer managementIsSatisfied;
    @ApiModelProperty("(样品管理需求)备注")
    private String managementRemark;

    @ApiModelProperty("(其他)标准要求")
    private String otherRequirements;
    @ApiModelProperty("(其他)准备情况")
    private String otherReadiness;
    @ApiModelProperty("(其他)是否满足")
    private Integer otherIsSatisfied;
    @ApiModelProperty("(其他)备注")
    private String otherRemark;

    @ApiModelProperty("确认时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime confirmDate;

    @ApiModelProperty("确认人员")
    private String confirmUser;

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

