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
 * 培训计划
 * </p>
 *
 * @author 
 * @since 2024-10-11 01:11:49
 */
@Getter
@Setter
@TableName("cnas_person_training")
@ApiModel(value = "PersonTraining对象", description = "培训计划")
public class PersonTraining implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("计划年份")
    private String planYear;

    @ApiModelProperty("编制人id")
    private Integer compilerId;

    @ApiModelProperty("编制日期")
    private LocalDateTime compilationDate;

    @ApiModelProperty("审核人id")
    private Integer reviewerId;

    @ApiModelProperty("审核日期")
    private LocalDateTime auditDate;

    @ApiModelProperty("审核状态")
    private Integer reviewerStatus;

    @ApiModelProperty("审核备注")
    private String auditRemarks;

    @ApiModelProperty("批准人id")
    private Integer approverId;

    @ApiModelProperty("批准备注")
    private String approvalRemarks;

    @ApiModelProperty("批准状态(1：批准；2：不批准)")
    private Integer approvalStatus;

    @ApiModelProperty("批准日期")
    private LocalDateTime approvalDate;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
}
