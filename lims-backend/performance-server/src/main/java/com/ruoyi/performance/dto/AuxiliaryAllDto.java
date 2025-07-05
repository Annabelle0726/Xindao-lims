package com.ruoyi.performance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 工时统计列表
 *
 * @Author zhuo
 * @Date 2024/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryAllDto {

    @ApiModelProperty("产量工时")
    private BigDecimal yieldHour;

    @ApiModelProperty("辅助工时")
    private BigDecimal subsidiaryHour;

    @ApiModelProperty("总工时")
    private BigDecimal totalHour;

    @ApiModelProperty("人员id")
    private Integer userId;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("月份")
    private String month;

}
