package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 原材料厂家进货验证检验项
 *
 * @author zhuo
 * @since 2024-09-26
 */
@Data
@TableName("ins_order_factory_verify_item")
public class InsOrderFactoryVerifyItem {


    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer factoryVerifyItemId;

    @ApiModelProperty(value = "外键: 进厂验证表 id")
    private Integer factoryVerifyId;

    @ApiModelProperty(value = "验证项目")
    private String inspectionItem;

    @ApiModelProperty(value = "验证结果")
    private String result;

    @ApiModelProperty(value = "排序")
    @TableField(fill = FieldFill.INSERT)
    private Integer sort;

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

