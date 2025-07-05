package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验项目的结果
 * @TableName ins_product_result
 */
@TableName(value ="ins_product_result")
@Data
public class InsProductResult implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外键：检验项目id")
    private Integer insProductId;

    @ApiModelProperty("检验值")
    private String insValue;

    @ApiModelProperty("计算值")
    private String comValue;

    @ApiModelProperty("设备值")
    private String equipValue;

    @ApiModelProperty("设备名称")
    private String equipName;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
