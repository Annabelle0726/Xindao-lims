package com.ruoyi.process.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/8
 */
@Data
public class QualityMonitorDetailsUpload {

    @ExcelProperty("监控目的")
    private String monitorPurpose;

    @ExcelProperty("计划开展时间")
    private String plannedTime;

    @ExcelProperty("监控项目")
    private String monitorProject;

    @ExcelProperty("参加单位（人员）")
    private String participant;

    @ExcelProperty("预算（元）")
    private String budget;

    @ExcelProperty("组织人员")
    private String organization;

    @ExcelProperty("监控方式")
    private String monitorWay;
}
