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
 * 检验项偏差预警详情表
 * </p>
 *
 * @author
 * @since 2025-03-28 02:18:58
 */
@Getter
@Setter
@TableName("ins_product_deviation_warning_detail")
@ApiModel(value = "InsProductDeviationWarningDetail对象", description = "检验项偏差预警详情表")
public class InsProductDeviationWarningDetail implements Serializable {

    @TableId(value = "deviation_warning_detail_id", type = IdType.AUTO)
    private Integer deviationWarningDetailId;

    @ApiModelProperty("主表id")
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

    @ApiModelProperty("检测值")
    private String testValue;

    @ApiModelProperty("检测时间")
    private LocalDateTime detectionTime;

    @ApiModelProperty("是否是问题数据 0: 否, 1: 是")
    private Integer isIssue;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
