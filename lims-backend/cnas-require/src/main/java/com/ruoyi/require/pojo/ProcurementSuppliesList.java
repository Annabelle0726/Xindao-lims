package com.ruoyi.require.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务与供应商 耗材列表
 * </p>
 *
 * @author 
 * @since 2024-11-15 04:04:32
 */
@Getter
@Setter
@TableName("procurement_supplies_list")
@ApiModel(value = "ProcurementSuppliesList对象", description = "服务与供应商 耗材列表")
public class ProcurementSuppliesList implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("编号")
    @ExcelProperty("编号")
    private Long id;

    @ApiModelProperty("目录id")
    private Long contentsId;

    @ApiModelProperty("耗材类型")
    @ExcelProperty("类别")
    private String consumablesType;

    @ApiModelProperty("货号")
    @ExcelProperty("货号")
    private String itemNumber;

    @ApiModelProperty("耗材名称")
    @ExcelProperty("名称")
    private String consumablesName;

    @ApiModelProperty("规格")
    @ExcelProperty("规格")
    private String specifications;

    @ApiModelProperty("计量单位")
    @ExcelProperty("计量单位")
    private String unit;

    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    @ApiModelProperty("存放位置")
    private Integer contentId;

    @ApiModelProperty("负责人")
    private Integer personInCharge;

    @ApiModelProperty("库存上限")
    private Integer upperLimit;

    @ApiModelProperty("库存下限")
    @ExcelProperty("库存下限")
    private Integer lowerLimit;

    @ApiModelProperty("供应商")
    private Integer supplier;

    @ApiModelProperty("耗材图标")
    private String consumablesIcon;

    @ApiModelProperty("耗材附件")
    private String attachment;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("当前库存数量")
    @ExcelProperty("当前库存")
    private Integer currentAmount;
}
