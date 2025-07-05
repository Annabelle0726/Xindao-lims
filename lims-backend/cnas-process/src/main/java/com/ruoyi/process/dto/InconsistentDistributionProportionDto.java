package com.ruoyi.process.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 占比对象
 *
 * @Author zhuo
 * @Date 2024/11/15
 */

@Data
public class InconsistentDistributionProportionDto {

    @ApiModelProperty("章节号")
    private String chapterNumber;

    @ApiModelProperty("要素")
    private String essentials;

    @ApiModelProperty("主任")
    private BigDecimal director;

    @ApiModelProperty("技术负责人")
    private BigDecimal technology;

    @ApiModelProperty("质量负责人")
    private BigDecimal quality;

    @ApiModelProperty("综合室")
    private BigDecimal comprehensive;

    @ApiModelProperty("试验室")
    private BigDecimal testing;

    @ApiModelProperty("合计")
    private Integer total;

    @ApiModelProperty("占比")
    private BigDecimal proportion;

}
