package com.ruoyi.device.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceStateExport {
    @ExcelProperty(value = "设备名称")
    private String deviceName;

    @ExcelProperty(value = "规格型号")
    private String specificationModel;

    @ExcelProperty(value = "管理编号")
    private String managementNumber;

    @ExcelProperty("设备状态")
    private String deviceStatus;

    @ExcelProperty("停用启用理由")
    private String reason;

    @ExcelProperty("提交人")
    private String createUser;

    @ExcelProperty("提交日期")
    private LocalDateTime createTime;

    @ExcelProperty("当前状态")
    private String currentState;

    @ExcelProperty("当前负责人")
    private String currentResponsible;
}
