package com.ruoyi.require.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeCalibrationScheduleExport {
    @ExcelProperty("仪器名称")
    private String instrumentName;

    @ExcelProperty("规格型号")
    private String model;

    @ExcelProperty("管理编号")
    private Integer managementNumber;

    @ExcelProperty("技术指标")
    private String technicalIndicators;

    @ExcelProperty("检定周期")
    private String verificationCyde;

    @ExcelProperty("检定单位")
    private String verificationUnit;

    @ExcelProperty("最近检定时间")
    private LocalDateTime recentlyTime;

    @ExcelProperty("计划下次检定时间")
    private LocalDateTime nextTime;

    @ExcelProperty("备注")
    private String remark;
}
