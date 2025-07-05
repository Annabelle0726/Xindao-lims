package com.ruoyi.inspect.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.inspect.pojo.InsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 铜单丝下单信息
 *
 * @Author zhuo
 * @Date 2024/9/13
 */
@Data
public class CopperInsOrderDto extends InsOrder {

    @ApiModelProperty("到货数量")
    private BigDecimal qtyArrived;

    @ApiModelProperty("单位")
    private String buyUnitMeas;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("批号")
    private String updateBatchNo;

    @ApiModelProperty("接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime declareDate;


}
