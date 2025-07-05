package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-11-15 03:47:19
 */
@Getter
@Setter
@TableName("procurement_supplies_expends")
@ApiModel(value = "ProcurementSuppliesExpends对象", description = "")
public class ProcurementSuppliesExpends implements Serializable {


    @ApiModelProperty("主表Id")
    @TableId(value = "expend_id", type = IdType.AUTO)
    private Long expendId;

    @ApiModelProperty("耗材Id")
    private Long listId;

    @ApiModelProperty("消耗数量")
    private Integer amount;

    @ApiModelProperty("录入人id")
    private Integer enterUserId;

    @ApiModelProperty("更新人id")
    private Integer updateUserId;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
