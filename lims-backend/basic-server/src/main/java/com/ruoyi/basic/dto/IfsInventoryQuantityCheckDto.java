package com.ruoyi.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 原材料查询, 排除了供应商
 * @Author zhuo
 * @Date 2024/8/28
 */
@Data
public class IfsInventoryQuantityCheckDto implements Serializable {

    private Integer id;

    @ApiModelProperty("域")
    private String contract;

    @ApiModelProperty("订单号")
    private String orderNo;


    @ApiModelProperty("行号")
    private String lineNo;

    @ApiModelProperty("下达号")
    private String releaseNo;

    @ApiModelProperty("接收号")
    private Integer receiptNo;

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("零件描述")
    private String partDesc;

    @ApiModelProperty("状态描述")
    private String status;

    @ApiModelProperty("状态")
    private String statusDb;

    @ApiModelProperty("抵达的采购数量")
    private BigDecimal qtyArrived;

    @ApiModelProperty("已检验的购买数量")
    private BigDecimal qtyInspected;

    @ApiModelProperty("要检验的采购数量")
    private BigDecimal qtyToInspect;

    @ApiModelProperty("抵达的库存数量")
    private BigDecimal invQtyInStore;

    @ApiModelProperty("抵达的采购数量")
    private BigDecimal purQtyInStore;

    @ApiModelProperty("配置标识")
    private String configurationId;

    @ApiModelProperty("批号")
    private String lotBatchNo;

    @ApiModelProperty("wdr号")
    private String waivDevRejNo;

    @ApiModelProperty("活动序列")
    private Integer activitySeq;


    @ApiModelProperty("序列号")
    private String serialNo;

    @ApiModelProperty("库位号")
    private String locationNo;

    @ApiModelProperty("版本号")
    private String engChgLevel;

    @ApiModelProperty("接收人")
    private String receiver;

    @ApiModelProperty("接收人姓名")
    private String receiverName;

    @ApiModelProperty("采购员")
    private String buyerCode;

    @ApiModelProperty("采购员姓名")
    private String buyerName;

    @ApiModelProperty("实际到货日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime arriveDate;

    @ApiModelProperty("实际交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime deliveryDate;

    @ApiModelProperty("生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime productDate;

    @ApiModelProperty("失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime invalidDate;


    @ApiModelProperty("审批日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime approvedDate;

    @ApiModelProperty("采购申请创建人")
    private String reqCeater;

    @ApiModelProperty("采购申请创建人姓名")
    private String reqCeaterName;

    @ApiModelProperty("采购订单行备注")
    private String lineRemarks;

    @ApiModelProperty("单位")
    private String buyUnitMeas;


    private Integer isSource;

    private Integer number;


    private Integer state;

    @ApiModelProperty("接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime receiverDate;

    @ApiModelProperty("是否为首次出现 0 否 1 是")
    private Integer isFirst;

    @ApiModelProperty("目标库位号")
    private String toLocation;

    @ApiModelProperty("是否是报检 0 否 1 是")
    private Integer isInspect;

    @ApiModelProperty("报检人")
    private String declareUser;


    @ApiModelProperty("报检人id")
    private Integer declareUserId;

    @ApiModelProperty("单位")
    private String partUnit;

    @ApiModelProperty("产业链检测数据")
    private String industryChain;

    @ApiModelProperty("报检时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime declareDate;

    @ApiModelProperty("是否卡可以季度检验 0 否 1 是")
    private Integer isQuarter;

    // 修改后的
    @ApiModelProperty("批号")
    private String updateBatchNo;

    @ApiModelProperty("是否是铜单丝, 0否, 1是")
    private Integer isCopper;

    @ApiModelProperty("物料类型")
    private Integer isExpire;

}
