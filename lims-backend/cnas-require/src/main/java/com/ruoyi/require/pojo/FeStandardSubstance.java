package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标准物质清单
 * </p>
 *
 * @author
 * @since 2024-11-13 03:58:59
 */
@Getter
@Setter
@TableName("cnas_fe_standard_substance")
@ApiModel(value = "FeStandardSubstance对象", description = "标准物质清单")
public class FeStandardSubstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标准物质名称")
    private String name;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("生产厂家")
    private String factoryManufacturer;

    @ApiModelProperty("出场编号")
    private String factoryNum;

    @ApiModelProperty("管理编号")
    private String manageNum;

    @ApiModelProperty("不确定度")
    private String uncertainty;

    @ApiModelProperty("数量")
    private Long quantity;

    @ApiModelProperty("购置日期")
    private LocalDateTime acquisitionDate;

    @ApiModelProperty("有效期")
    private LocalDateTime effectiveDate;

    @ApiModelProperty("文档编号")
    private String fileNum;

    @ApiModelProperty("存放位置")
    private String position;

    @ApiModelProperty("借调状态（0:未借调 1:已借调）")
    private Integer state;

    @ApiModelProperty("备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
