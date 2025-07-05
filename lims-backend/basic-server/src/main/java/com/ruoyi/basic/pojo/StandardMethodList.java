package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准树下的标准列表
 * @TableName standard_method_list
 */
@Data
public class StandardMethodList implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标准编号
     */
    @ApiModelProperty("标准编号")
    private String code;

    /**
     * 标准名称
     */
    @ApiModelProperty("标准称号")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("工厂")
    private String factory;

    @ApiModelProperty("实验室")
    private String laboratory;

    @ApiModelProperty("样品分类")
    private String sampleType;

    @ApiModelProperty("样品")
    private String sample;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(exist = false)
    private String createUserName;
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
    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
    /**
     *
     */
    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
