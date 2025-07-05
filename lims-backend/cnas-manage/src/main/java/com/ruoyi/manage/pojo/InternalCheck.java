package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内审检查表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Data
@TableName("cnas_internal_check")
public class InternalCheck {

    @TableId(type = IdType.AUTO)
    private Integer checkId;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty("部门负责人")
    private String departmentHead;

    @ApiModelProperty("审核员")
    private String auditor;

    @ApiModelProperty("审核日期")
    private String reviewDate;

    @ApiModelProperty("编制人id")
    private Integer writeUserId;

    @ApiModelProperty("编制人")
    private String writeUserName;

    @ApiModelProperty("编制时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;

    @ApiModelProperty("批准人id")
    private Integer ratifyUserId;

    @ApiModelProperty("批准人")
    private String ratifyUserName;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态, 0 不通过, 1通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准内容")
    private String ratifyRemark;

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

