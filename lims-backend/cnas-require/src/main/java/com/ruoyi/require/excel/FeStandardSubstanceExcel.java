package com.ruoyi.require.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeStandardSubstanceExcel {
    @ExcelProperty("标准物质名称")
    private String name;

    @ExcelProperty("规格型号")
    private String model;

    @ExcelProperty("生产厂家")
    private String factoryManufacturer;

    @ExcelProperty("出场编号")
    private String factoryNum;

    @ExcelProperty("管理编号")
    private String manageNum;

    @ExcelProperty("不确定度")
    private String uncertainty;

    @ExcelProperty("数量")
    private Long quantity;

    @ExcelProperty("购置日期")
    private LocalDateTime acquisitionDate;

    @ExcelProperty("有效期")
    private LocalDateTime effectiveDate;

    @ExcelProperty("文档编号")
    private String fileNum;

    @ExcelProperty("存放位置")
    private String position;

    @ExcelProperty("借调状态")
    private Integer state;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建人")
    private String createUser;

    @ExcelProperty("创建日期")
    private LocalDateTime createTime;
}
