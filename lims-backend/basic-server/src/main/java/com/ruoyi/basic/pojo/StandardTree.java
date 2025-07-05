package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 标准树
* @TableName standard_tree
*/
@TableName(value ="standard_tree")
@Data
public class StandardTree implements Serializable {

    /**
    *
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 工厂
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("工厂")
    @Length(max= 255,message="编码长度不能超过255")
    private String factory;
    /**
    * 实验室
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("实验室")
    @Length(max= 255,message="编码长度不能超过255")
    private String laboratory;
    /**
    * 样品大类
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("样品大类")
    @Length(max= 255,message="编码长度不能超过255")
    private String sampleType;
    /**
    * 样品
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("样品")
    @Length(max= 255,message="编码长度不能超过255")
    private String sample;
    /**
    * 型号
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("型号")
    @Length(max= 255,message="编码长度不能超过255")
    private String model;
    /**
    *
    */
    @ApiModelProperty("")
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

    /**
     * 修改前名称
     */
    @TableField(exist = false,select = false)
    private String oldModel;
}
