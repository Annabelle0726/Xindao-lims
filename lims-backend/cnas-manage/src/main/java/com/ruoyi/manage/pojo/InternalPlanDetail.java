package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 内审年度计划详情表
 * </p>
 *
 * @author
 * @since 2024-11-13 03:28:48
 */
@Data
@TableName("cnas_internal_plan_detail")
@ApiModel(value = "InternalPlanDetail对象", description = "内审年度计划详情表")
public class InternalPlanDetail {

    @TableId(value = "plan_detail_id", type = IdType.AUTO)
    private Integer planDetailId;

    @ApiModelProperty("内审年度计划id")
    private Integer planId;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty("一月")
    private String january;

    @ApiModelProperty("二月")
    private String february;

    @ApiModelProperty("三月")
    private String march;

    @ApiModelProperty("四月")
    private String april;

    @ApiModelProperty("五月")
    private String may;

    @ApiModelProperty("六月")
    private String june;

    @ApiModelProperty("七月")
    private String july;

    @ApiModelProperty("八月")
    private String august;

    @ApiModelProperty("九月")
    private String september;

    @ApiModelProperty("十月")
    private String october;

    @ApiModelProperty("十一月")
    private String november;

    @ApiModelProperty("十二月")
    private String december;

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
