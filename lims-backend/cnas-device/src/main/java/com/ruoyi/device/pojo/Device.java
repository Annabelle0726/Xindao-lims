package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备(Device)表对象
 */
@TableName(value = "device")
@Data
public class Device implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "en设备名称")
    private String enDeviceName;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "出厂编号")
    private String factoryNo;

    @ApiModelProperty(value = "管理编号")
    private String managementNumber;

    @ApiModelProperty(value = "技术指标")
    private String technicalIndicators;

    @ApiModelProperty(value = "购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acquisitionDate;

    @ApiModelProperty(value = "校准有效日期")
    private LocalDateTime activationDate;

    @ApiModelProperty(value = "管理人Id")
    private Integer equipmentManager;

    @ApiModelProperty(value = "存放点")
    private String storagePoint;

    @ApiModelProperty(value = "所属部门Id")
    private Integer subordinateDepartmentsId;

    @ApiModelProperty(value = "检验项目Id")
    private String insProductIds;

    @ApiModelProperty(value = "校准服务机构")
    private String calibrationServices;

    @ApiModelProperty(value = "最近校准日期")
    private LocalDateTime lastCalibrationDate;

    @ApiModelProperty(value = "下次校准日期")
    private LocalDateTime nextCalibrationDate;

    @ApiModelProperty(value = "设备类型")
    private String largeCategory;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "设备状态")
    private Integer deviceStatus;

    @ApiModelProperty(value = "校准周期(月)")
    private String calibrationDate;

    @ApiModelProperty(value = "图片上传")
    private String imageUpload;

    @ApiModelProperty(value = "图片备注")
    private String imageName;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("数采-文件后缀")
    private String fileType;

    @ApiModelProperty("数采-采集地址")
    private String collectUrl;

    @ApiModelProperty("数采-存储地址")
    private String storageUrl;

    @ApiModelProperty("数采-设备IP")
    private String ip;

    @ApiModelProperty("数采-是否为数采设备")
    @TableField(exist = false)
    private Boolean isItADataAcquisitionDevice;

    @ApiModelProperty("数采-委托字段")
    private String entrustCode;

    @ApiModelProperty("数采-样品字段")
    private String sampleCode;

    @ApiModelProperty("数采-db，mdb文件名称")
    private String dbFileName;

    @ApiModelProperty("被授权人")
    private String authorizedPerson;

    @ApiModelProperty("资产编码")
    private String assetCode;

    @ApiModelProperty("产地")
    private String origin;

}
