package com.ruoyi.performance.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 工时统计的修正工时
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-29 02:38:19
 */
@Getter
@Setter
@TableName("auxiliary_correction_hours")
@ApiModel(value = "AuxiliaryCorrectionHours对象", description = "工时统计的修正工时")
public class AuxiliaryCorrectionHours  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("姓名id")
    private Integer nameUser;

    @ApiModelProperty("类型")
    @ExcelProperty(value = "类型")
    private String type;

    @ApiModelProperty("1")
    @ExcelProperty(value = "1日")
    private BigDecimal oneHours;

    @ApiModelProperty("2")
    @ExcelProperty(value = "2日")
    private BigDecimal twoHours;

    @ApiModelProperty("3")
    @ExcelProperty(value = "3日")
    private BigDecimal threeHours;

    @ApiModelProperty("4")
    @ExcelProperty(value = "4日")
    private BigDecimal fourHours;

    @ApiModelProperty("5")
    @ExcelProperty(value = "5日")
    private BigDecimal fiveHours;

    @ApiModelProperty("6")
    @ExcelProperty(value = "6日")
    private BigDecimal sixHours;

    @ApiModelProperty("7")
    @ExcelProperty(value = "7日")
    private BigDecimal sevenHours;

    @ApiModelProperty("8")
    @ExcelProperty(value = "8日")
    private BigDecimal eightHours;

    @ApiModelProperty("9")
    @ExcelProperty(value = "9日")
    private BigDecimal nineHours;

    @ApiModelProperty("10")
    @ExcelProperty(value = "10日")
    private BigDecimal tenHours;

    @ApiModelProperty("11")
    @ExcelProperty(value = "11日")
    private BigDecimal elevenHours;

    @ApiModelProperty("12")
    @ExcelProperty(value = "12日")
    private BigDecimal twelveHours;

    @ApiModelProperty("13")
    @ExcelProperty(value = "13日")
    private BigDecimal thirteenHours;

    @ApiModelProperty("14")
    @ExcelProperty(value = "14日")
    private BigDecimal fourteenHours;

    @ApiModelProperty("15")
    @ExcelProperty(value = "15日")
    private BigDecimal fifteenHours;

    @ApiModelProperty("16")
    @ExcelProperty(value = "16日")
    private BigDecimal sixteenHours;

    @ApiModelProperty("17")
    @ExcelProperty(value = "17日")
    private BigDecimal seventeenHours;

    @ApiModelProperty("18")
    @ExcelProperty(value = "18日")
    private BigDecimal eighteenHours;

    @ApiModelProperty("19")
    @ExcelProperty(value = "19日")
    private BigDecimal nineteenHours;

    @ApiModelProperty("20")
    @ExcelProperty(value = "20日")
    private BigDecimal twentyHours;

    @ApiModelProperty("21")
    @ExcelProperty(value = "21日")
    private BigDecimal twentyOneHours;

    @ApiModelProperty("22")
    @ExcelProperty(value = "22日")
    private BigDecimal twentyTwoHours;

    @ApiModelProperty("23")
    @ExcelProperty(value = "23日")
    private BigDecimal twentyThreeHours;

    @ApiModelProperty("24")
    @ExcelProperty(value = "24日")
    private BigDecimal twentyFourHours;

    @ApiModelProperty("25")
    @ExcelProperty(value = "25日")
    private BigDecimal twentyFiveHours;

    @ApiModelProperty("26")
    @ExcelProperty(value = "26日")
    private BigDecimal twentySixHours;

    @ApiModelProperty("27")
    @ExcelProperty(value = "27日")
    private BigDecimal twentySevenHours;

    @ApiModelProperty("28")
    @ExcelProperty(value = "28日")
    private BigDecimal twentyEightHours;

    @ApiModelProperty("29")
    @ExcelProperty(value = "29日")
    private BigDecimal twentyNineHours;

    @ApiModelProperty("30")
    @ExcelProperty(value = "30日")
    private BigDecimal thirtyHours;

    @ApiModelProperty("31")
    @ExcelProperty(value = "31日")
    private BigDecimal thirtyOneHours;

    @ApiModelProperty("月份")
    @ExcelProperty(value = "月份")
    private String month;

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
}
