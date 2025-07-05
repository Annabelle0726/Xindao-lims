package com.ruoyi.performance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PerformanceShiftAddDto {

    @NotNull(message = "请选择班次")
    @ApiModelProperty("班次")
    private String shift;

    @NotNull(message = "请选择员工")
    @ApiModelProperty("员工id")
    private String userId;

    @NotNull(message = "请选择周次")
    @ApiModelProperty("开始周次")
    private LocalDateTime startWeek;

    @NotNull(message = "请选择周次")
    @ApiModelProperty("结束周次")
    private LocalDateTime endWeek;
}
