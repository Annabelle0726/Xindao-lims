package com.ruoyi.process.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/8
 */
@Data
public class QualitySuperviseDetailsUpload {

    @ExcelProperty("监督日期")
    private String superviseTime;

    @ExcelProperty("监督目的")
    private String supervisePurpose;

    @ExcelProperty("监控项目")
    private String superviseProject;

    @ExcelProperty("被监督人员")
    private String supervisee;

    @ExcelProperty("监督原因")
    private String superviseReason;

    @ExcelProperty("备注")
    private String remark;
}
