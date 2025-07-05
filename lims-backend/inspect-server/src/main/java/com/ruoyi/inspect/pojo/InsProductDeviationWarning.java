package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检验项偏差预警主表
 * </p>
 *
 * @author
 * @since 2025-03-28 02:18:02
 */
@Getter
@Setter
@TableName("ins_product_deviation_warning")
@ApiModel(value = "InsProductDeviationWarning对象", description = "检验项偏差预警主表")
public class InsProductDeviationWarning implements Serializable {

    @TableId(value = "deviation_warning_id", type = IdType.AUTO)
    private Integer deviationWarningId;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("样品id")
    private Integer insSampleId;

    @ApiModelProperty("检验项id")
    private Integer insProductId;

    @ApiModelProperty("订单编号")
    private String entrustCode;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("偏差值")
    private String deviationValue;

    @ApiModelProperty("检测时间")
    private LocalDateTime detectionTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
