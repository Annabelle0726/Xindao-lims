package com.ruoyi.require.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StoreExcel {
    @ExcelProperty("入库单号")
    private String oddNumbers;

    @ExcelProperty("耗材名称")
    private String consumablesName;

    @ExcelProperty("入库数量")
    private Integer storeNumber;

    @ExcelProperty("入库总价")
    private Double totalPrice;

    @ExcelProperty("入库人")
    private String storageUserName;

    @ExcelProperty("入库日期")
    private LocalDate storageTime;

    @ExcelProperty("说明")
    private String remark;

    @ExcelProperty("登记人")
    private String registrantName;

    @ExcelProperty("登记日期")
    private LocalDate registrantTime;

}
