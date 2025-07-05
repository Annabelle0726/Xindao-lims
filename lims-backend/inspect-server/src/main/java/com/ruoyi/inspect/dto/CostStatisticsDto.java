package com.ruoyi.inspect.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CostStatisticsDto{

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value ={"中天科技检测中心样品登记表","ZTT/QR-30-01-01","日期"})
    private LocalDateTime createTime;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","ZTT/QR-30-01-01","检验编号"})
    private String entrustCode;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","样品名称"})
    private String sample;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","规格型号"})
    private String model;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","样品数量"})
    private Integer num;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","总价"})
    private BigDecimal price;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","试验项目"})
    private String inspectionItem;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","委托单位"})
    private String company;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","委托人"})
    private String name;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","生产单位"})
    private String production;

    @ExcelProperty(value ={"中天科技检测中心样品登记表","工程名称"})
    private String engineering;

    @ExcelIgnore
    private Integer createUser;

    @ExcelIgnore
    private Integer insSampleId;

    @ExcelIgnore
    private Double cost;//工时

    @TableField(exist = false,select = false)
    @ExcelIgnore
    private String dates;


}
