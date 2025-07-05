package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-19
 */
@Data
@TableName("product_supplier_density")
public class ProductSupplierDensity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("厂家名称")
    private String supplierName;

    @ApiModelProperty("密度值")
    private String densityValue;

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

