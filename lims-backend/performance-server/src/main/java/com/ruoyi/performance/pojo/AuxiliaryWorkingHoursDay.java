package com.ruoyi.performance.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 日工时管理的辅助工时
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 02:22:19
 */
@Data
@TableName("auxiliary_working_hours_day")
@ApiModel(value = "AuxiliaryWorkingHoursDay对象", description = "日工时管理的辅助工时")
@ExcelIgnoreUnannotated
public class AuxiliaryWorkingHoursDay  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("姓名id")
    private Integer nameUser;

    @ApiModelProperty("编号")
    @ExcelProperty(index = 2, value = "编号")
    private String number;

    @ApiModelProperty("辅助项目名称")
    @ExcelProperty(index = 3, value = "辅助项目名称")
    private String auxiliaryProject;

    @ApiModelProperty("核准工时")
    @ExcelProperty(index = 5, value = "核准工时")
    private BigDecimal approvedWorkingHour;

    @ApiModelProperty("数量")
    @ExcelProperty(index = 6, value = "数量")
    private Integer amount;

    @ApiModelProperty("辅助工时")
    @ExcelProperty(index = 7, value = "辅助工时")
    private BigDecimal nonproductiveTime;

    @ApiModelProperty("辅助说明")
    @ExcelProperty(index = 8, value = "辅助说明")
    private String remarks;

    @ApiModelProperty("复核人")
    @ExcelProperty(index = 9, value = "复核人")
    private String reviewer;

    @ApiModelProperty("复核数量")
    @ExcelProperty(index = 10, value = "复核数量")
    private Integer reviewerNumber;

    @ApiModelProperty("复核工时")
    @ExcelProperty(index = 11, value = "复核工时")
    private BigDecimal reviewerNonproductiveTime;

    @ApiModelProperty("复核说明")
    @ExcelProperty(index = 12, value = "复核说明")
    private String reviewerRemark;

    @ApiModelProperty("年")
    @ExcelProperty(index = 13, value = "年")
    private String year;

    @ApiModelProperty("班次")
    @ExcelProperty(index = 14, value = "班次")
    private String shift;

    @ApiModelProperty("周次")
    @ExcelProperty(index = 15, value = "周次")
    private String week;

    @ApiModelProperty("星期")
    @ExcelProperty(index = 16, value = "星期")
    private String weekDay;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("状态")
    @ExcelProperty(index = 4, value = "状态")
    private String state;

    @ApiModelProperty("日期")
    @ExcelProperty(index = 17, value = "日期")
    private String dateTime;
}
