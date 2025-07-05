package com.ruoyi.inspect.pojo;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单费用表
 *
 * @author zhuo
 * @since 2025-02-28
 */
@Data
@TableName("ins_order_rates")
public class InsOrderRates {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("样品id")
    private Integer insSampleId;

    @ApiModelProperty("检验项id")
    private Integer insProductId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("样品编号")
    private String entrustCode;

    @ApiModelProperty("检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty("检测项目")
    private String inspectionItem;

    @ApiModelProperty("检验项子类")
    private String inspectionItemSubclass;

    @ApiModelProperty("电缆标识")
    private String cableTag;

    @ApiModelProperty("标准价格")
    private String rates;

    @ApiModelProperty("分组")
    private String manHourGroup;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

}

