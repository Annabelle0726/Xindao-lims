package com.ruoyi.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StructureTestObjectData {
    @ExcelProperty(value = "场所")
    private String laboratory;

    @ExcelProperty(value = "检测对象")
    private String specimenName;

    @ExcelProperty(value = "检测对象(EN)")
    private String specimenNameEn;

    @ExcelProperty(value = "对象代号")
    private String code;

    @ExcelProperty(value = "产品名称")
    private String name;

    @ExcelProperty(value = "产品名称(EN)")
    private String nameEn;
}
