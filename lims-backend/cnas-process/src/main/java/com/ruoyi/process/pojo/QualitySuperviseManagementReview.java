package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 质量监督管理评审输入材料
 * </p>
 *
 * @author
 * @since 2025-05-07 10:46:11
 */
@Getter
@Setter
@TableName("cnas_quality_supervise_management_review")
@ApiModel(value = "QualitySuperviseManagementReview对象", description = "质量监督管理评审输入材料")
public class QualitySuperviseManagementReview implements Serializable {

    @TableId(value = "management_review_id", type = IdType.AUTO)
    private Integer managementReviewId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("计划内容")
    private String implementationContent;

    @ApiModelProperty("动态内容")
    private String dynamicContent;

    @ApiModelProperty("监督人")
    private String supervisor;

    @ApiModelProperty("质量评审日期")
    private LocalDate superviseDate;

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
