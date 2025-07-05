package com.ruoyi.basic.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ifs_inventory_quantity")
@ExcelIgnoreUnannotated
public class IfsInventoryQuantity  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty("域")
    private String contract;

    @ExcelProperty(index = 13, value = "订单号")
    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("行号")
    private String lineNo;

    @ApiModelProperty("下达号")
    private String releaseNo;

    @ApiModelProperty("接收号")
    private Integer receiptNo;

    @ExcelProperty(index = 3, value = "零件号")
    @ApiModelProperty("零件号")
    private String partNo;

    @ExcelProperty(index = 4, value = "零件描述")
    @ApiModelProperty("零件描述")
    private String partDesc;

    @ApiModelProperty("状态描述(IFS原本拉取的状态)")
    private String status;

    @ApiModelProperty("状态(IFS原本拉取的状态)")
    private String statusDb;

    @ExcelProperty(index = 11, value = "抵达的采购数量")
    @ApiModelProperty("抵达的采购数量")
    private BigDecimal qtyArrived;

    @ApiModelProperty("已检验的购买数量")
    private BigDecimal qtyInspected;

    @ApiModelProperty("要检验的采购数量")
    private BigDecimal qtyToInspect;

    @ApiModelProperty("供应商编号")
    private String supplierId;

    @ExcelProperty(index = 5, value = "供应商名称")
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

    @ExcelProperty(index = 12, value = "单位")
    @ApiModelProperty("单位")
    private String buyUnitMeas;

    @ApiModelProperty("是否为ifs拉取,0 否, 1是")
    private Integer isSource;

    private Integer number;


    @ApiModelProperty("状态: 0:待报检, 1:待检验, :已审核")
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
    @ExcelProperty(index = 1, value = "批号")
    @ApiModelProperty("批号")
    private String updateBatchNo;

    @ApiModelProperty("是否结束, 0否, 1是")
    private Integer isFinish;

    @ApiModelProperty("是否是铜单丝, 0否, 1是")
    private Integer isCopper;

    // 合格状态,: 0 检验中, 1合格, 2不合格, 3未下单,4让步放行
    @ApiModelProperty("检验状态")
    private Integer inspectStatus;

    @ApiModelProperty("是否采购订单登记: 0否, 1:是")
    private Integer isRegister;

    @ApiModelProperty("是否修改过批号: 0否, 1:是")
    private Integer isUpdateBatch;

    // 是否是过期材料: 0否, 1:是"
    @ApiModelProperty("物料类型")
    private Integer isExpire;
}
