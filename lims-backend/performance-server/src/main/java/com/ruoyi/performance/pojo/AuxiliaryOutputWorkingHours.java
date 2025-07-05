package com.ruoyi.performance.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 日工时管理的产量工时
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 03:48:48
 */
@Getter
@Setter
@TableName("auxiliary_output_working_hours")
@ApiModel(value = "AuxiliaryOutputWorkingHours对象", description = "日工时管理的产量工时")
@ExcelIgnoreUnannotated
public class AuxiliaryOutputWorkingHours  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("检测项分类")
    @ExcelProperty(index = 2, value = "检测项分类")
    private String inspectionItemClass;

    @ApiModelProperty("检测项")
    @ExcelProperty(index = 3, value = "检测项")
    private String inspectionItem;

    @ApiModelProperty("检测子项")
    @ExcelProperty(index = 4, value = "检测子项")
    private String inspectionItemSubclass;

    @ApiModelProperty("样品id")
    private Integer sampleId;

    @ApiModelProperty("样品编号")
    @ExcelProperty(index = 6, value = "样品编号")
    private String sample;

    @ApiModelProperty("加班委托单号")
    private String overtimeOrderNo;

    @ApiModelProperty("加班工时")
    private BigDecimal overtimeWorkTime;

    @ApiModelProperty("加班数量")
    private Integer overtimeAmount;

    @ApiModelProperty("非加班委托单号")
    private Integer orderId;

    @ApiModelProperty("委托单号")
    @ExcelProperty(index = 5, value = "委托单号")
    private String orderNo;

    @ApiModelProperty("工时")
    private BigDecimal workTime;

    @ApiModelProperty("数量")
    private Integer amount;

    @ApiModelProperty("产量工时")
    @ExcelProperty(index = 7, value = "产量工时")
    private BigDecimal outputWorkTime;

    @ApiModelProperty("日期")
    @ExcelProperty(index = 10, value = "日期")
    private String dateTime;

    @ApiModelProperty("周次")
    @ExcelProperty(index = 11, value = "周次")
    private String week;

    @ApiModelProperty("星期")
    @ExcelProperty(index = 12, value = "星期")
    private String weekDay;

    @ApiModelProperty("检测人id")
    @TableField("`check`")
    private Integer check;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("工时分组")
    @ExcelProperty(index = 8, value = "工时分组")
    private String manHourGroup;

    @ApiModelProperty("单价")
    @ExcelProperty(index = 9, value = "单价")
    private BigDecimal price;

    @ApiModelProperty("检验项id")
    private Integer insProductId;
}
