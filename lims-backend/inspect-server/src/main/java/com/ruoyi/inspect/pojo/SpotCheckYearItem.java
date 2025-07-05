package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 年度抽样计划详情表
 *
 * @author zhuo
 * @since 2024-10-10
 */
@TableName(value = "spot_check_year_item")
@Data
public class SpotCheckYearItem {

    @TableId(type = IdType.AUTO)
    private Integer yearItemId;
    //主表id
    @ApiModelProperty("主表id")
    private Integer yearId;

    @ApiModelProperty("类别")
    private String yearType;

    @ApiModelProperty("一月")
    private String january;

    @ApiModelProperty("二月")
    private String february;

    @ApiModelProperty("三月")
    private String march;

    @ApiModelProperty("四月")
    private String april;

    @ApiModelProperty("五月")
    private String may;

    @ApiModelProperty("六月")
    private String june;

    @ApiModelProperty("七月")
    private String july;

    @ApiModelProperty("八月")
    private String august;

    @ApiModelProperty("九月")
    private String september;

    @ApiModelProperty("十月")
    private String october;

    @ApiModelProperty("十一月")
    private String november;

    @ApiModelProperty("十二月")
    private String december;

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

