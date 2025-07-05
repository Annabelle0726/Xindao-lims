package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 检验项目厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-23
 */
@TableName(value ="standard_product_list_supplier_ask")
@Data
public class StandardProductListSupplierAsk {

    @TableId(type = IdType.AUTO)
    private Integer supplierAskId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("检验项目id")
    private Long productListId;

    @ApiModelProperty("厂家名称")
    private String supplierName;

    @ApiModelProperty("要求值")
    private String ask;

    @ApiModelProperty("要求描述")
    private String tell;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;


    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

