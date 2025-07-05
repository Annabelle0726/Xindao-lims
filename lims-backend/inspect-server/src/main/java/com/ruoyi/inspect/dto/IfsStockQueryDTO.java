package com.ruoyi.inspect.dto;

import lombok.Data;

@Data
public class IfsStockQueryDTO {
	private String partNo;
	private String partDescription; // 零件描述
	private String partDesc;
	private String locNo; // 库位号
	private String locDesc; // 库位描述
	private String lotBatchNo; // 批号
	private String wdrNo; // WDR编号
	private String warehouse; // 仓库
	private String quantityFlag; // 数量标识(现有数量)
	private String locationGroup; // 库位组
	private String locationGroupDesc; // 库位组描述
	private String reelNumber; // 载具编号
	private String startMeter; // 起始米标
	private String endMeter; // 截止米标
	private String outerColor; // 外护颜色
	private String insulationColor; // 绝缘颜色
	private String otcOrderNo; // 物料属性里面的OTC订单号
	private String mpsNo; // 生产计划号
	private String letteringInfo; // 印字信息
	private String sStockQuantity; // 库存数量2
	private String availableStockQuantity; // 可用库存数量
	private String sAvailableStockQuantity; // 库用库存数量2
	private String sunit; // 单位2
	private String stockSource; // 来源
	private String inspInstorageTime; // 检测入库日期
	private String testRemark; // 检测备注
	private String grossWeight; // 毛重
	private String netWeight; // 净重
	private String packingManner; // 包装方式
	private String cylinderNumber; //打圈个数
	private String systemNo; //系统号
	private String remark; //备注
	private String customerName; //客户名称
	private String reserveQuantity; //预留数量
	private String lengthRequirement; // 判断要求
	private String inSource;//入库来源
	private String splitQuality;//分割预留数量

	//接口 getIfsStockReport 用到的字段
	private String attr23;//入库来源
	private String attr6;//otc订单号
	private String attr21;//客户名称
	private String attr4;//外护颜色
	private String attr5;//绝缘颜色
	private String attr8;//印字信息
	private String lot_batch_no;//批次号
	private String location_no;//库位号
	private String attr17;//包装方式
	private String attr1;//载具编号

	//接口queryColReservedInfoStd 用到的两个字段
	private String cotcOrderNo; // 表里面的OTC订单号
	private String cotcLineNo; // 表里面的OTC行号

	private Boolean isAll;//是否查询全部库存

	private Long page;
	private Long limit;
}
