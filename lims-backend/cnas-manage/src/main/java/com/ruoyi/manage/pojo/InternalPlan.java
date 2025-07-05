package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 内审年度计划
 * </p>
 *
 * @author
 * @since 2024-11-13 03:27:47
 */
@Data
@TableName("cnas_internal_plan")
@ApiModel(value = "InternalPlan对象", description = "内审年度计划")
public class InternalPlan {

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    @ApiModelProperty("内审目的")
    private String purpose;

    @ApiModelProperty("内生范围")
    private String scope;

    @ApiModelProperty("内审依据")
    private String basis;

    @ApiModelProperty("组长")
    private String leader;

    @ApiModelProperty("组员")
    private String crew;

    @ApiModelProperty("编制人id")
    private Integer writeUserId;

    @ApiModelProperty("编制人")
    private String writeUserName;

    @ApiModelProperty("编制时间")
    private LocalDateTime writeTime;

    @ApiModelProperty("审核人id")
    private Integer examineUserId;

    @ApiModelProperty("审核人")
    private String examineUserName;

    @ApiModelProperty("审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty("审核状态,0 不通过, 1 通过")
    private Integer examineStatus;

    @ApiModelProperty("审核信息")
    private String examineRemark;

    @ApiModelProperty("批准人id")
    private Integer ratifyUserId;

    @ApiModelProperty("批准人")
    private String ratifyUserName;

    @ApiModelProperty("批准时间")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态,0 不通过, 1 通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准信息")
    private String ratifyRemark;

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
}
