package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("product_part")
public class ProductPart implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("色标")
    private String colorCode;

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
