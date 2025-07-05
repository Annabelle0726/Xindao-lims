package com.ruoyi.inspect.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.inspect.pojo.InsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaoaoy
 * @version 1.0.0
 * @create 2024/3/14 18:46
 **/
@Data
@ExcelIgnoreUnannotated
public class SampleOrderDto extends InsOrder {

    @ApiModelProperty("检验对象")
    private String sampleType;

    @ExcelProperty(index = 2, value = "样品名称")
    @ApiModelProperty("样品名称")
    private String sampleName;

    @ExcelProperty(index = 3, value = "样品型号")
    @ApiModelProperty("样品型号")
    private String sampleModel;

    private String assign;

    @ExcelProperty(index = 4, value = "样品数量")
    @ApiModelProperty("样品数量")
    private Integer sampleNum;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ExcelProperty(index = 8, value = "检验进度%")
    @ApiModelProperty("检验进度%")
    private String insProgress;

    @ApiModelProperty("报告id")
    private String reportId;

    @TableField("`url`")
    private String url;

    private String urlS;

    private String name;

    @ExcelProperty(index = 5, value = "检验人")
    @ApiModelProperty("检验人")
    private String testingName;

    /**
     * (报告导出)紧急程度 （0普通 1优先 2紧急）
     */
    @ExcelProperty(index = 6, value = "紧急程度")
    @ApiModelProperty("紧急程度 （0普通 1优先 2紧急）")
    private String typeString;

    /**
     * (报告导出)检验结果
     */
    @ExcelProperty(index = 10, value = "检验结果")
    @ApiModelProperty("检验结果")
    private String insResultString;

    /**
     * (报告导出)不合格项目
     */
    @ApiModelProperty("不合格项")
    @ExcelProperty(index = 13, value = "不合格项")
    private String unqualifiedItem;

    /**
     * (报告导出)下单时间
     */
    @ExcelProperty(index = 11, value = "下单时间")
    @ApiModelProperty("下单时间")
    private String createTimeString;

    @ApiModelProperty("子实验室")
    private String sonLaboratory;

    private String sampleStr;


    @ApiModelProperty("总价")
    private String totalPrice;

    @ApiModelProperty("订单id(导出用)")
    private String ids;


}
