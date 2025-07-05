package com.ruoyi.performance.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class AuxiliaryOutputWorkingHoursDto extends AuxiliaryOutputWorkingHours {

    @ApiModelProperty("检测人")
    @ExcelProperty(index = 1, value = "检测人")
    private String name;

    @ApiModelProperty("电缆标识")
    private String cableTag;
}
