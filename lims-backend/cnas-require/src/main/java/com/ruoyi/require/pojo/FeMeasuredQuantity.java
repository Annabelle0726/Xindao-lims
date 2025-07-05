package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-电源稳定性-测定量
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:44
 */
@Getter
@Setter
@TableName("cnas_fe_measured_quantity")
@ApiModel(value = "FeMeasuredQuantity对象", description = "设施和环境条件-设施和环境条件要求-电源稳定性-测定量")
public class FeMeasuredQuantity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "measured_quantity_id", type = IdType.AUTO)
    private Integer measuredQuantityId;

    @ApiModelProperty("测定量名称")
    private String measuredQuantityLabel;

    @ApiModelProperty("值A")
    private String valueA;

    @ApiModelProperty("值B")
    private String valueB;

    @ApiModelProperty("值C")
    private String valueC;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("电源稳定性id")
    private Integer powerStableId;
}
