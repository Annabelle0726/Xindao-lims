package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 培训计划详情
 * </p>
 *
 * @author
 * @since 2024-10-11 01:46:27
 */
@Getter
@Setter
@TableName("cnas_person_training_detailed")
@ApiModel(value = "PersonTrainingDetailed对象", description = "培训计划详情")
public class PersonTrainingDetailed implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训计划")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("培训目标")
    private String trainingObjectives;

    @ApiModelProperty("培训内容")
    private String trainingContent;

    @ApiModelProperty("培训方式")
    private String trainingMode;

    @ApiModelProperty("状态(1：已完成；2：待评价；3: 未开始)")
    private Integer state;

    @ApiModelProperty("参加对象")
    private String participants;

    @ApiModelProperty("举办部门")
    private String holdingDepartment;

    @ApiModelProperty("培训地点")
    private String placeTraining;

    @ApiModelProperty("培训讲师_id")
    private Integer trainingLecturerId;

    @ApiModelProperty("计划培训日期")
    private String trainingDate;

    @ApiModelProperty("培训完成时间")
    private LocalDate openingTime;

    @ApiModelProperty("课时")
    private Integer classHour;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("培训计划id")
    private Integer planId;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人id", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "更新人id", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("考核方式")
    private String assessmentMethod;

    @ApiModelProperty("本次培训综合评价")
    private String comprehensiveAssessment;

    @ApiModelProperty("评价人")
    private Integer assessmentUserId;

    @ApiModelProperty("评价时间")
    private LocalDate assessmentDate;
}
