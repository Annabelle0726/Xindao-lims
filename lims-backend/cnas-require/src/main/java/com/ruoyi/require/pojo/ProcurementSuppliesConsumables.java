package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("procurement_supplies_consumables")
@ApiModel("耗材采购明细表")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcurementSuppliesConsumables {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("耗材主表id")
    private Integer storeId;

    @ApiModelProperty("耗材名称")
    private String consumablesName;

    @ApiModelProperty("单价")
    private Double unitPrice;

    @ApiModelProperty("入库数量")
    private Integer storeNumber;

    @ApiModelProperty("总价")
    private Double totalPrice;

    @ApiModelProperty("货号")
    private String itemNumber;

    @ApiModelProperty("类别")
    private String type;

    @ApiModelProperty("规格")
    private String specifications;

    @ApiModelProperty("参考供应商")
    private String supplier;

    @ApiModelProperty("计量单位")
    private String unit;

}
