package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName("procurement_supplies_store")
@ApiModel("耗材入库表")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcurementSuppliesStore implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("目录id")
    private Integer contentsId;

    @ApiModelProperty("入库单号")
    private String oddNumbers;

    @ApiModelProperty("入库库存")
    private String inventory;

    @ApiModelProperty("入库总金额")
    private Double totalAmount;

    @ApiModelProperty("入库人")
    private Integer storageUser;

    @ApiModelProperty("入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate storageTime;

    @ApiModelProperty("入库说明")
    private String remark;

    @ApiModelProperty("登记人")
    private Integer registrant;

    @ApiModelProperty("登记时间")
    private LocalDate registrantTime;

}
