package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内审检查详情表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Data
@TableName("cnas_internal_check_detail")
public class InternalCheckDetail {

    @TableId(type = IdType.AUTO)
    private Integer checkDetailId;

    @ApiModelProperty("检查主表id")
    private Integer checkId;

    @ApiModelProperty("要素条款")
    private String element;

    @ApiModelProperty("审核内容")
    private String content;

    @ApiModelProperty("审核方式")
    private String method;

    @ApiModelProperty("审核结果记录")
    private String resultRecords;

    @ApiModelProperty("不符合性质")
    private String nonNature;

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

