package com.ruoyi.require.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.require.pojo.ProcurementSuppliesList;
import lombok.Data;

@Data
public class ProcurementSuppliesListDto extends ProcurementSuppliesList {
    @ExcelProperty("参考供应商")
    private String supplierName; // 供应商名称
    @ExcelProperty("更新人")
    private String updateUserName; // 更新人名称
    @ExcelProperty("负责人")
    private String personInChargeName; // 负责人名称
}
