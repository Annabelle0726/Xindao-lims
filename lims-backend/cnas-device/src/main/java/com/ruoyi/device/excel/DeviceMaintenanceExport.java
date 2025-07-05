package com.ruoyi.device.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class DeviceMaintenanceExport {

    @ColumnWidth(20)
    @ExcelProperty("设备名称")
    private String deviceName;

    @ColumnWidth(20)
    @ExcelProperty("流程编号")
    private String deviceNumber;

    @ColumnWidth(20)
    @ExcelProperty("管理编号")
    private String managementNumber;

    @ColumnWidth(50)
    @ExcelProperty("维护内容")
    private String content;

    @ColumnWidth(20)
    @ExcelProperty("维护时间")
    private String date;

    @ColumnWidth(20)
    @ExcelProperty("下次维护时间")
    private String nextDate;

    @ColumnWidth(20)
    @ExcelProperty("维护类型")
    private String maintenanceType;

    @ColumnWidth(20)
    @ExcelProperty("维护人")
    private String name;

    @ColumnWidth(50)
    @ExcelProperty("备注")
    private String comments;
}
