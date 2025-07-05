package com.ruoyi.inspect.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcurementSuppliesListEDto {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("编号")
    @ExcelProperty("编号")
    private Long id;

    @ApiModelProperty("耗材类型")
    @ExcelProperty("类别")
    private String consumablesType;

    @ApiModelProperty("货号")
    @ExcelProperty("货号")
    private String itemNumber;

    @ApiModelProperty("耗材名称")
    @ExcelProperty("名称")
    private String consumablesName;

    @ApiModelProperty("规格")
    @ExcelProperty("规格")
    private String specifications;

    @ApiModelProperty("计量单位")
    @ExcelProperty("计量单位")
    private String unit;




    @ApiModelProperty("库存下限")
    @ExcelProperty("库存下限")
    private Integer lowerLimit;



    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    private String remark;


    @ApiModelProperty("当前库存数量")
    @ExcelProperty("当前库存")
    private Integer currentAmount;
}
