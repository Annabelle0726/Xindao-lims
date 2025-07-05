package com.ruoyi.require.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class FeLightningProtectionExcel {
    @ExcelProperty("原文件名")
    private String fileName;

    @ExcelProperty("检测日期")
    private String detectionDate;

    @ExcelProperty("有效期")
    private String termValidity;

    @ExcelProperty("检测单位")
    private String detectionUnit;

    @ExcelProperty("创建时间")
    private String createTime;
}
