package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 季度抽样计划详情表
 *
 * @author makejava
 * @since 2024-10-09
 */
@TableName(value ="spot_check_quarter_item")
@Data
public class SpotCheckQuarterItem {

    @TableId(type = IdType.AUTO)
    private Integer quarterItemId;

    @ApiModelProperty("主表id")
    private Integer quarterId;

    @ApiModelProperty("产品类型")
    private String productType;

    @ApiModelProperty("产品型号")
    private String productModel;

    @ApiModelProperty("责任人")
    private String dutyUser;

    @ApiModelProperty("抽样数量")
    private String spotCheckNumber;

    @ApiModelProperty("抽样时间")
    private String spotCheckTime;

    @ApiModelProperty("试样结论")
    private String result;

    @ApiModelProperty("取样人员")
    private String samplingUser;

    @ApiModelProperty("备注")
    private String itemRemark;

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

