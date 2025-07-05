package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准模板
 * @TableName standard_template
 */
@TableName(value ="standard_template")
@Data
public class StandardTemplate  implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 模板结构
     */
    private String thing;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     *
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
    /**
     *
     */
    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty("更新用户")
    @TableField(exist = false)
    private String updateUserName;

    /**
     * 模板编号
     */
    @ApiModelProperty("模板编号")
    private String number;
}
