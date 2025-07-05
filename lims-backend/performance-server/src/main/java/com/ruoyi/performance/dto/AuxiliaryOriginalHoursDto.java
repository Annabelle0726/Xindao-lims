package com.ruoyi.performance.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
//原始工时
public class AuxiliaryOriginalHoursDto {

    @ApiModelProperty("姓名")
    @ExcelProperty(value = "姓名")
    private String name;

    //修正工时
    private AuxiliaryCorrectionHours auxiliaryCorrectionHours;

    @ApiModelProperty("类型")
    @ExcelProperty(value = "类型")
    private String type;

    @ApiModelProperty("1日工时")
    @ExcelProperty(value = "1日")
    private BigDecimal oneHours;

    @ExcelIgnore
    private Integer one;

    @ApiModelProperty("2日工时")
    @ExcelProperty(value = "2日")
    private BigDecimal twoHours;

    @ExcelIgnore
    private Integer  two;

    @ApiModelProperty("3日工时")
    @ExcelProperty(value = "3日")
    private BigDecimal threeHours;

    @ExcelIgnore
    private Integer three;

    @ApiModelProperty("4日工时")
    @ExcelProperty(value = "4日")
    private BigDecimal fourHours;

    @ExcelIgnore
    private Integer four;

    @ApiModelProperty("5日工时")
    @ExcelProperty(value = "5日")
    private BigDecimal fiveHours;

    @ExcelIgnore
    private Integer  five;

    @ApiModelProperty("6日工时")
    @ExcelProperty(value = "6日")
    private BigDecimal sixHours;

    @ExcelIgnore
    private Integer six;

    @ApiModelProperty("7日工时")
    @ExcelProperty(value = "7日")
    private BigDecimal sevenHours;

    @ExcelIgnore
    private Integer seven;

    @ApiModelProperty("8日工时")
    @ExcelProperty(value = "8日")
    private BigDecimal eightHours;

    @ExcelIgnore
    private Integer eight;

    @ApiModelProperty("9日工时")
    @ExcelProperty(value = "9日")
    private BigDecimal nineHours;

    @ExcelIgnore
    private Integer nine;

    @ApiModelProperty("10日工时")
    @ExcelProperty(value = "10日")
    private BigDecimal tenHours;

    @ExcelIgnore
    private Integer ten;

    @ApiModelProperty("11日工时")
    @ExcelProperty(value = "11日")
    private BigDecimal elevenHours;

    @ExcelIgnore
    private Integer eleven;

    @ApiModelProperty("12日工时")
    @ExcelProperty(value = "12日")
    private BigDecimal twelveHours;

    @ExcelIgnore
    private Integer twelve;

    @ApiModelProperty("13日工时")
    @ExcelProperty(value = "13日")
    private BigDecimal thirteenHours;

    @ExcelIgnore
    private Integer thirteen;

    @ApiModelProperty("14日工时")
    @ExcelProperty(value = "14日")
    private BigDecimal fourteenHours;

    @ExcelIgnore
    private Integer fourteen;

    @ApiModelProperty("15日工时")
    @ExcelProperty(value = "15日")
    private BigDecimal fifteenHours;

    @ExcelIgnore
    private Integer fifteen;

    @ApiModelProperty("16日工时")
    @ExcelProperty(value = "16日")
    private BigDecimal sixteenHours;

    @ExcelIgnore
    private Integer sixteen;

    @ApiModelProperty("17日工时")
    @ExcelProperty(value = "17日")
    private BigDecimal seventeenHours;

    @ExcelIgnore
    private Integer seventeen;

    @ApiModelProperty("18日工时")
    @ExcelProperty(value = "18日")
    private BigDecimal eighteenHours;

    @ExcelIgnore
    private Integer eighteen;

    @ApiModelProperty("19日工时")
    @ExcelProperty(value = "19日")
    private BigDecimal nineteenHours;

    @ExcelIgnore
    private Integer nineteen;

    @ApiModelProperty("20日工时")
    @ExcelProperty(value = "20日")
    private BigDecimal twentyHours;

    @ExcelIgnore
    private Integer twenty;

    @ApiModelProperty("21日工时")
    @ExcelProperty(value = "21日")
    private BigDecimal twentyOneHours;

    @ExcelIgnore
    private Integer twentyOne;

    @ApiModelProperty("22日工时")
    @ExcelProperty(value = "22日")
    private BigDecimal twentyTwoHours;

    @ExcelIgnore
    private Integer twentyTwo;

    @ApiModelProperty("23日工时")
    @ExcelProperty(value = "23日")
    private BigDecimal twentyThreeHours;

    @ExcelIgnore
    private Integer twentyThree;

    @ApiModelProperty("24日工时")
    @ExcelProperty(value = "24日")
    private BigDecimal twentyFourHours;

    @ExcelIgnore
    private Integer twentyFour;

    @ApiModelProperty("25日工时")
    @ExcelProperty(value = "25日")
    private BigDecimal twentyFiveHours;

    @ExcelIgnore
    private Integer twentyFive;

    @ApiModelProperty("26日工时")
    @ExcelProperty(value = "26日")
    private BigDecimal twentySixHours;

    @ExcelIgnore
    private Integer twentySix;

    @ApiModelProperty("27日工时")
    @ExcelProperty(value = "27日")
    private BigDecimal twentySevenHours;

    @ExcelIgnore
    private Integer twentySeven;

    @ApiModelProperty("28日工时")
    @ExcelProperty(value = "28日")
    private BigDecimal twentyEightHours;

    @ExcelIgnore
    private Integer  twentyEight;

    @ApiModelProperty("29日工时")
    @ExcelProperty(value = "29日")
    private BigDecimal twentyNineHours;

    @ExcelIgnore
    private Integer  twentyNine;

    @ApiModelProperty("30日工时")
    @ExcelProperty(value = "30日")
    private BigDecimal thirtyHours;

    @ExcelIgnore
    private Integer thirty;

    @ApiModelProperty("31日工时")
    @ExcelProperty(value = "31日")
    private BigDecimal thirtyOneHours;

    @ExcelIgnore
    private Integer thirtyOne;

    @ApiModelProperty("总工时")
    @ExcelProperty(value = "总工时")
    private BigDecimal total;

    @ApiModelProperty("月份")
    @ExcelProperty(value = "月份")
    private String month;

}
