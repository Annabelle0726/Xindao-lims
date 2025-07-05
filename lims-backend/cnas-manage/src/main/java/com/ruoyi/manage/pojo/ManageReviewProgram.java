package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 
 * @since 2024-11-09 03:05:42
 */
@Getter
@Setter
@TableName("cnas_manage_review_program")
@ApiModel(value = "ManageReviewProgram对象", description = "")
public class ManageReviewProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("评审时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty("评审地点")
    private String judgingLocation;

    @ApiModelProperty("评审目的")
    private String judgingPurpose;

    @ApiModelProperty("评审方式")
    private String judgingMethod;

    @ApiModelProperty("参加人员")
    private String participants;

    @ApiModelProperty("评审范围")
    private String judgingScope;

    @ApiModelProperty("评审依据")
    private String judgingBasis;

    @ApiModelProperty("评审主要内容")
    private String mainContext;

    @ApiModelProperty("准备工作要求")
    private String preparationRequirements;

    @ApiModelProperty("编制")
    private String editor;

    @ApiModelProperty("编制日期")
    private LocalDateTime editorDate;

    @ApiModelProperty("批准")
    private String approve;

    @ApiModelProperty("批准日期")
    private LocalDateTime approveDate;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
