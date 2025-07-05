package com.ruoyi.inspect.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UnqualifiedHandlerVO extends InsUnqualifiedHandler {

    @ApiModelProperty("不合格处理主键id")
    private Long handlerId;

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
    private String statusDB;

    @ApiModelProperty("抵达的采购数量")
    private BigDecimal qtyArrived;

    @ApiModelProperty("已检验的购买数量")
    private BigDecimal qtyInspected;

    @ApiModelProperty("要检验的采购数量")
    private BigDecimal qtyToInspect;

    @ApiModelProperty("供应商编号")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

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
    private String activitySeq;


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

    @ApiModelProperty("采购单位")
    private String buyUnitMeas;

    /**
     * 是否为ifs拉取 1:是 0:否
     */
    @ApiModelProperty("是否为ifs拉取 1:是 0:否")
    private Integer isSource;

    /**
     * 拆分数量
     */
    @ApiModelProperty("拆分数量")
    private Integer number;


    /**
     * 检验状态
     */
    @ApiModelProperty("检验状态")
    private Integer state;

    @ApiModelProperty("接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime receiverDate;

}
