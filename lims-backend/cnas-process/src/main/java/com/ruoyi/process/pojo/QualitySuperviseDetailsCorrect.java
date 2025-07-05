package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 质量监督详情纠正处理表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_supervise_details_correct")
@ApiModel(value = "QualitySuperviseDetailsCorrect对象", description = "质量监督详情纠正处理表")
public class QualitySuperviseDetailsCorrect {

    @TableId(type = IdType.AUTO)
    private Integer superviseDetailsCorrectId;

    @ApiModelProperty("监督详情id")
    private Integer superviseDetailsId;

    @ApiModelProperty("人员培训详情Id")
    private Integer personTrainingDetailedId;

    @ApiModelProperty("0不合格描述")
    private String raiseResult;

    @ApiModelProperty("0vde专家发现")
    private String vdeRaiseResult;

    @ApiModelProperty("0提出部门")
    private String raiseDepartment;

    @ApiModelProperty("0提出人id")
    private Integer raiseUserId;

    @ApiModelProperty("0提出人")
    private String raiseUserName;

    @ApiModelProperty("0提出时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate raiseTime;

    @ApiModelProperty("1原因分析")
    private String causeResult;

    @ApiModelProperty("1原因分析责任部门")
    private String causeDepartment;

    @ApiModelProperty("1原因分析人id")
    private Integer causeUserId;

    @ApiModelProperty("1原因分析人")
    private String causeUserName;

    @ApiModelProperty("1原因分析时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate causeTime;

    @ApiModelProperty("2纠正措施")
    private String correctResult;

    @ApiModelProperty("2提出部门确认")
    private String raiseDepartmentAffirm;

    @ApiModelProperty("2纠正责任部门")
    private String correctDepartment;

    @ApiModelProperty("2纠正人id")
    private Integer correctUserId;

    @ApiModelProperty("2纠正人")
    private String correctUserName;

    @ApiModelProperty("2纠正时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate correctTime;

    @ApiModelProperty("3验证结果")
    private String validationResult;

    @ApiModelProperty("3验证部门")
    private String validationDepartment;

    @ApiModelProperty("3验证人id")
    private Integer validationUserId;

    @ApiModelProperty("3验证人")
    private String validationUserName;

    @ApiModelProperty("3验证时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validationTime;

    @ApiModelProperty("是否结束, 0: 未结束, 1:已结束")
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
    @ApiModelProperty("流程, 0:不合格提出, 1:原因分析, 2:纠正措施, 3:验证结果")
    private Integer flowType;

}

