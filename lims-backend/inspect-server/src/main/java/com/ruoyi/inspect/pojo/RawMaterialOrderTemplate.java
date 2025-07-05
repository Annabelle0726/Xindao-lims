package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 原材料下单模板
 *
 * @author zhuo
 * @since 2024-08-05
 */
@TableName(value = "raw_material_order_template")
@Data
public class RawMaterialOrderTemplate {

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
     * 模板名称
     */
    @ApiModelProperty("零件编号")
    private String partNo;

    /**
     * 模板内容
     */
    @ApiModelProperty("模板内容")
    private String thing;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

