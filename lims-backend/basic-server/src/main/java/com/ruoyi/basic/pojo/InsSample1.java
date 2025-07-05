package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验样品
 * @TableName ins_sample
 */
@TableName(value ="ins_sample")
@Data
public class InsSample1 implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 1：合格 0：不合格
     */
    private Integer insResult;

    /**
     * 外键：ins_order表id
     */
    private Integer insOrderId;

    /**
     * 配套样品型号
     */
    private String joinModel;

    /**
     * 配套样品名称
     */
    private String joinName;

    /**
     * 配套样品数量
     */
    private Integer joinNum;

    /**
     * 样品编码
     */
    private String sampleCode;

    /**
     * 检验工厂
     */
    private String factory;

    /**
     * 实验室名称
     */
    private String laboratory;

    /**
     * 样品类型
     */
    private String sampleType;

    /**
     * 样品名称
     */
    private String sample;

    /**
     * 规格型号
     */
    private String model;

    /**
     * 检验状态(0：待检验1:检验中 2:已检验3：待复核4：复核未通过5：复核通过)
     */
    private Integer insState;

    /**
     * 备注
     */
    private String remark;

    private Integer standardMethodListId;

    @ApiModelProperty("样品单位")
    private String unit;

    private Integer cellId;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer parentId;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("特殊标准方法")
    private String specialStandardMethod;

    @TableField(select = false,exist = false)
    private Integer num=1;

}
