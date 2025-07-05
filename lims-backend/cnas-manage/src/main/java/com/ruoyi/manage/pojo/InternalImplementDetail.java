package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内审实施计划详情
 *
 * @author makejava
 * @since 2024-11-11
 */
@Data
@TableName("cnas_internal_implement_detail")
public class InternalImplementDetail {

    @TableId(type = IdType.AUTO)
    private Integer implementDetailId;

    @ApiModelProperty("实施计划主表id")
    private Integer implementId;

    @ApiModelProperty("时间")
    private String implement;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty("责任人")
    private String responsible;

    @ApiModelProperty("审核员")
    private String auditor;

    @ApiModelProperty("审核内容")
    private String reviewContent;

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

    // 导出使用
    @TableField(select = false, exist = false)
    private Integer index;
}

