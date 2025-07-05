package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/8/12
 */
@Data
public class EnterFactoryReport {

    @ApiModelProperty("材料名称")
    private String sample;

    @ApiModelProperty("到货数量")
    private String qtyArrived;

    @ApiModelProperty("抽检数量")
    private String quantity;

    @ApiModelProperty("规格型号")
    private String partDesc;

    @ApiModelProperty("材料厂家")
    private String supplierName;

    @ApiModelProperty("批号")
    private String lotBatchNo;

    @ApiModelProperty("检测编号")
    private String code;
}
