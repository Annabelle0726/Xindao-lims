package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准树下的检验项目
 * @TableName standard_product_list
 */
@TableName(value ="standard_product_list")
@Data
public class StandardProductList implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 检验项
     */
    @ApiModelProperty("检验项")
    private String inspectionItem;

    @ApiModelProperty("检验项EN")
    private String inspectionItemEn;

    /**
     * 检验项小类
     */
    @ApiModelProperty("检验项小类")
    private String inspectionItemSubclass;

    @ApiModelProperty("检验项小类EN")
    private String inspectionItemSubclassEn;

    /**
     * 实验室
     */
    @ApiModelProperty("实验室")
    private String laboratory;

    @ApiModelProperty("子实验室")
    private String sonLaboratory;

    /**
     * 计量单位
     */
    @ApiModelProperty("计量单位")
    private String unit;

    /**
     * 单价(元)
     */
    @ApiModelProperty("单价")
    private String price;

    /**
     * 工时(H)
     */
    @ApiModelProperty("工时")
    private String manHour;

    /**
     * 工时分组
     */
    @ApiModelProperty("工时分组")
    private String manHourGroup;

    /**
     * 检验项类型
     */
    @ApiModelProperty("检验项类型")
    private String inspectionItemType;

    /**
     * 检验值类型
     */
    @ApiModelProperty("检验值类型")
    private String inspectionValueType;

    /**
     * 检验次数
     */
    @ApiModelProperty("检验次数")
    private Integer checkoutNumber;

    /**
     * 区间
     */
    @ApiModelProperty("区间")
    private String section;

    /**
     * 区间
     */
    @ApiModelProperty("芯数区间")
    private String cores;

    /**
     * 方法
     */
    @ApiModelProperty("方法列表")
    private String method;

    @ApiModelProperty("方法")
    private String methodS;

    /**
     * 预计时间(天)
     */
    @ApiModelProperty("预计时间")
    private Integer manDay;

    /**
     * 特殊标识
     */
    @ApiModelProperty("特殊标识")
    private String bsm;

    /**
     * 要求值
     */
    @ApiModelProperty("要求值")
    private String ask;

    @ApiModelProperty("要求描述")
    private String tell;

    /**
     * 外键：标准方法id
     */
    @ApiModelProperty("标准方法id")
    private Integer standardMethodListId;

    @ApiModelProperty("工厂")
    private String factory;

    @ApiModelProperty("样品分类")
    private String sampleType;

    @ApiModelProperty("样品")
    private String sample;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("模板id")
    private Integer templateId;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     *
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     *
     */
    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
    /**
     *
     */
    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("1：有效 0：无效")
    private Integer state;

    private String dic;

    private String tree;

    @ApiModelProperty("检验项id")
    private Integer structureItemParameterId;

    @ApiModelProperty(value = "检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty(value = "检验项分类EN")
    private String inspectionItemClassEn;

    @ApiModelProperty(value = "条件")
    private String radius;

    @ApiModelProperty(value = "条件列表")
    private String radiusList;

    @ApiModelProperty(value = "收费标准(元/次)")
    private String rates;

    @ApiModelProperty(value = "索引顺序")
    private Integer sort;

    /**
     * 导体材质
     */
    @ApiModelProperty("导体材质")
    private String conductorMaterial;

    /**
     * 导体类型
     */
    @ApiModelProperty("导体类型")
    private String conductorType;
}
