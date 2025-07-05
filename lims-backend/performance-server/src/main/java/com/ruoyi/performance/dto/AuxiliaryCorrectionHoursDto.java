package com.ruoyi.performance.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuxiliaryCorrectionHoursDto extends AuxiliaryCorrectionHours {

    @ApiModelProperty("姓名")
    @ExcelProperty(value = "姓名")
    private String name;

    @ApiModelProperty("部门")
    private String departLims;

    @ApiModelProperty("总工时")
    private BigDecimal total;
}
