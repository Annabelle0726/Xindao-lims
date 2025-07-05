package com.ruoyi.performance.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class AuxiliaryWorkingHoursDayDto extends AuxiliaryWorkingHoursDay {

    @ApiModelProperty("姓名")
    @ExcelProperty(index = 1, value = "姓名")
    private String name;
}
