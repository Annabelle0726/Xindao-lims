package com.ruoyi.inspect.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 偏差分析对象
 *
 * @Author zhuo
 * @Date 2024/10/25
 */
@Data
public class DeviationAnalyzeVo {

    @ApiModelProperty("厂家检测数据")
    private List<Object> supplierData;

    @ApiModelProperty("本地检测数据")
    private List<Object> localData;

    @ApiModelProperty("绝对偏差")
    private List<Object> absoluteDeviation;

    @ApiModelProperty("平均值")
    private List<Object> average;

    /**
     * 厂家
     */
    @ApiModelProperty("UCL")
    private List<Object> supplierULC;

    @ApiModelProperty("LCL")
    private List<Object> supplierLCL;

    @ApiModelProperty("平均值")
    private List<Object> supplierAverage;

    @ApiModelProperty("极差")
    private List<Object> supplierRange;

    /**
     * 本地
     */
    @ApiModelProperty("UCL")
    private List<Object> localULC;

    @ApiModelProperty("LCL")
    private List<Object> localLCL;

    @ApiModelProperty("平均值")
    private List<Object> localAverage;

    @ApiModelProperty("极差")
    private List<Object> localRange;
}
