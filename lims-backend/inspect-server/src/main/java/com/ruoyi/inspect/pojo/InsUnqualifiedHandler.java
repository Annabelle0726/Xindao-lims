package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 不合格处理表
 * @TableName ins_unqualified_handler
 */
@TableName(value ="ins_unqualified_handler")
@Data
public class InsUnqualifiedHandler implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String no;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String headline;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 生产批次
     */
    @ApiModelProperty(value = "生产批次")
    private String productionBatch;

    /**
     * 到货数量
     */
    @ApiModelProperty(value = "到货数量")
    private String cargoQuantity;

    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String specsModels;

    /**
     * 报检日期
     */
    @ApiModelProperty(value = "报检日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inspectTime;

    /**
     * 反馈人
     */
    @ApiModelProperty(value = "反馈人")
    private String feedbackUser;

    /**
     * 反馈日期
     */
    @ApiModelProperty(value = "反馈日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate feedbackTime;

    /**
     * 分类
     */
    @ApiModelProperty(value = "")
    private String classification;

    /**
     * 不合格归属
     */
    @ApiModelProperty(value = "不合格归属")
    private String offGradeAscription;

    /**
     * 不合格描述
     */
    @ApiModelProperty(value = "不合格描述")
    private String unqualifiedDesc;

    /**
     * 原材料检验id
     */
    @ApiModelProperty(value = "原材料检验id")
    private Integer inventoryQuantityId;

    /**
     * 原材料检验id
     */
    @ApiModelProperty(value = "订单id")
    private Integer insOrderId;

    /**
     * oa工作流id
     */
    @ApiModelProperty(value = "oa工作流id")
    private Integer requestId;

    /**
     * oa审核状态(1:待审核 2:审核中 3:通过 4:驳回)
     */
    @ApiModelProperty(value = "oa审核状态")
    private Integer oaState;

    /**
     * oa附件url地址
     */
    @ApiModelProperty("oa附件url地址")
    private String fileUrl;

}
