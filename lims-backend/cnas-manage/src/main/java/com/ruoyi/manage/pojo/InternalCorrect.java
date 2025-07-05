package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 内审管理纠正处理表
 * </p>
 *
 * @author 
 * @since 2024-11-13 04:00:15
 */
@Data
@TableName("cnas_internal_correct")
@ApiModel(value = "InternalCorrect对象", description = "内审管理纠正处理表")
public class InternalCorrect {

    @TableId(value = "correct_id", type = IdType.AUTO)
    private Integer correctId;

    @ApiModelProperty("不合格描述")
    private String raiseResult;

    @ApiModelProperty("vde专家发现")
    private String vdeRaiseResult;

    @ApiModelProperty("提出部门")
    private String raiseDepartment;

    @ApiModelProperty("提出人id")
    private Integer raiseUserId;

    @ApiModelProperty("提出人")
    private String raiseUserName;

    @ApiModelProperty("提出时间")
    private LocalDate raiseTime;

    @ApiModelProperty("原因分析")
    private String causeResult;

    @ApiModelProperty("原因分析责任部门")
    private String causeDepartment;

    @ApiModelProperty("原因分析人id")
    private Integer causeUserId;

    @ApiModelProperty("原因分析人")
    private String causeUserName;

    @ApiModelProperty("原因分析时间")
    private LocalDate causeTime;

    @ApiModelProperty("纠正措施")
    private String correctResult;

    @ApiModelProperty("提出部门确认")
    private String raiseDepartmentAffirm;

    @ApiModelProperty("纠正责任部门")
    private String correctDepartment;

    @ApiModelProperty("纠正人id")
    private Integer correctUserId;

    @ApiModelProperty("纠正")
    private String correctUserName;

    @ApiModelProperty("纠正时间")
    private LocalDate correctTime;

    @ApiModelProperty("验证结果")
    private String validationResult;

    @ApiModelProperty("验证部门")
    private String validationDepartment;

    @ApiModelProperty("验证人id")
    private Integer validationUserId;

    @ApiModelProperty("验证人")
    private String validationUserName;

    @ApiModelProperty("验证时间")
    private LocalDate validationTime;

    @ApiModelProperty("是否结束, 0: 未结束, 1:已结束")
    private Integer isFinish;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false,select = false)
    @ApiModelProperty("流程, 0:不符合工作情况记录, 1处理措施, 2:纠正措施, 3:是否通知客户可恢复工作")
    private Integer flowType;
}
