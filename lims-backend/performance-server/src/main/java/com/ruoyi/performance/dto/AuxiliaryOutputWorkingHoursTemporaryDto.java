package com.ruoyi.performance.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/3/16
 */
@Data
public class AuxiliaryOutputWorkingHoursTemporaryDto extends AuxiliaryOutputWorkingHoursTemporary {

    @ApiModelProperty("检测人")
    private String name;

    @ApiModelProperty("电缆标识")
    private String cableTag;
}
