package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单标准模板复制
 *
 * @author zhuo
 * @since 2024-11-05
 */
@Data
@TableName(value = "ins_order_standard_template")
public class InsOrderStandardTemplate {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("模板id")
    private Integer templateId;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("模板结构")
    private String thing;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人")
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("模板编号")
    private String number;

}

