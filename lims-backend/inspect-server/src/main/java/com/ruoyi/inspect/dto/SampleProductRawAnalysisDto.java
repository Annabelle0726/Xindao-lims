package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/10/17
 */
@Data
public class SampleProductRawAnalysisDto {

    @ApiModelProperty("样品id")
    private Integer insSampleId;

    @ApiModelProperty("检验项id")
    private Integer insProductId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("检验项名称")
    private String inspectionItem;

    @ApiModelProperty("检验结果")
    private String lastValue;

    @ApiModelProperty("合格判断, 1:合格, 0:不合格, 3:不判定")
    private Integer insResult;

    @ApiModelProperty("批次号")
    private String updateBatchNo;

    @ApiModelProperty("厂家名称")
    private String supplierName;

    @ApiModelProperty("样品名称")
    private String sample;


}
