package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 原材料厂家进货验证
 *
 * @author zhuo
 * @since 2024-09-26
 */
@Data
@TableName("ins_order_factory_verify")
public class InsOrderFactoryVerify {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer factoryVerifyId;

    @ApiModelProperty(value = "外键:  ins_order表 id")
    private Integer insOrderId;

    @ApiModelProperty(value = "委托编号")
    private String entrustCode;

    @ApiModelProperty("材料厂家")
    private String supplierName;

    @ApiModelProperty("到货日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime declareDate;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("材料批号")
    private String updateBatchNo;

    @ApiModelProperty(value = "验证文件编号")
    private String verifyFileCode;

    @ApiModelProperty(value = "1材料名称")
    private String basicName;

    @ApiModelProperty(value = "2规格型号")
    private String basicModel;

    @ApiModelProperty(value = "3材料批号")
    private String basicBatchNo;

    @ApiModelProperty(value = "4执行标准")
    private String basicStandard;

    @ApiModelProperty(value = "5生产日期")
    private String basicDate;

    @ApiModelProperty(value = "6供货数量")
    private String basicNumber;

    @ApiModelProperty(value = "7材料颜色")
    private String basicColor;

    @ApiModelProperty(value = "8其他(名称)")
    private String basicOtherValue;

    @ApiModelProperty(value = "8其他")
    private String basicOther;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false,select = false)
    @ApiModelProperty(value = "验证项目集合")
    private List<InsOrderFactoryVerifyItem>  factoryVerifyItemList;

}

