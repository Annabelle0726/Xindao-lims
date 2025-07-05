package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 岗位职责
 * </p>
 *
 * @author
 * @since 2024-10-09 02:07:49
 */
@Getter
@Setter
@TableName("cnas_person_job_responsibilities")
@ApiModel(value = "PersonJobResponsibilities对象", description = "岗位职责")
public class PersonJobResponsibilities implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("1岗位名称")
    private String postName;

    @ApiModelProperty("1工作目标")
    private String jobObjective;

    @ApiModelProperty("1岗位职责")
    private String jobResponsibilities;

    @ApiModelProperty("1任职人id")
    private String incumbentId;

    @ApiModelProperty("1 提交操作人")
    private String submittingOperator;

    @ApiModelProperty("1提交日期")
    private LocalDateTime submittingDate;

    @ApiModelProperty("2 任职人 主管id")
    private Integer supervisorId;

    @ApiModelProperty("2 任职人 操作人")
    private String incumbentOperator;

    @ApiModelProperty("2 任职人 日期")
    private LocalDateTime incumbentDate;

    @ApiModelProperty("3 主管 操作人")
    private String supervisorOperator;

    @ApiModelProperty("3 主管 日期")
    private LocalDateTime supervisorDate;

    @ApiModelProperty(value = "创建人id", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("当前状态")
    private String currentState;

    @ApiModelProperty(value = "创建日期 / 提交日期", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("当前负责人")
    private String currentResponsible;
}
