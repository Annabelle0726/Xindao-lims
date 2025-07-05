package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 检验委托单
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
@Getter
@Setter
@TableName("cnas_inspection_order")
@ApiModel(value = "InspectionOrder对象", description = "检验委托单")
public class InspectionOrder {

    @TableId(value = "inspection_order_id", type = IdType.AUTO)
    private Integer inspectionOrderId;

    @ApiModelProperty("成品订单id")
    private Integer insOrderId;

    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("试样名称")
    private String sampleName;

    @ApiModelProperty("型号")
    private String modelNo;

    @ApiModelProperty("生产单位")
    private String production;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("委托时间")
    private LocalDate commissionDate;

    @ApiModelProperty("委托单位")
    private String commissionUnit;

    @ApiModelProperty("委托人")
    private String commissionUser;

    @ApiModelProperty("委托人联系方式")
    private String commissionPhone;

    @ApiModelProperty("样品状态")
    private String sampleStatus;

    @ApiModelProperty("是否留样1: 是, 2:否")
    private Integer isLeave;

    @ApiModelProperty("样品处理方式 1：实验室处理 0：委托单位取回")
    private Integer processing;

    @ApiModelProperty("约定时间")
    private LocalDate appointed;

    @ApiModelProperty("报告发送方式 1：自取 0：其他")
    private Integer send;

    @ApiModelProperty("判断规则 1：考虑不确定度 0：不考虑不确定度")
    private Integer criterionRule;

    @ApiModelProperty("判断规则考虑不确定度备注")
    private String criterionRuleRemark;

    @ApiModelProperty("综合室人员")
    private String generalOfficeUser;

    @ApiModelProperty("接收日期")
    private LocalDate receiptData;

    @ApiModelProperty("领样人员")
    private String sampleTakerUser;

    @ApiModelProperty("领样日期")
    private LocalDate sampleData;

    @ApiModelProperty("报告文件地址")
    private String fileUrl;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
