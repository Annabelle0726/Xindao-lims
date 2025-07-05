package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 安全内务三废登记详情
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:54
 */
@Data
@TableName("cnas_internal_wastes_detail")
@ApiModel(value = "InternalWastesDetail对象", description = "安全内务三废登记详情")
public class InternalWastesDetail {

    @TableId(value = "wastes_detail_id", type = IdType.AUTO)
    private Integer wastesDetailId;

    @ApiModelProperty("主表id")
    private Integer wastesId;

    @ApiModelProperty("名称")
    private String designation;

    @ApiModelProperty("体积")
    private String volume;

    @ApiModelProperty("送处理日期")
    private String deliveryDate;

    @ApiModelProperty("移交人")
    private String transferPeople;

    @ApiModelProperty("接收人")
    private String acceptor;

    @ApiModelProperty("接收单位")
    private String receivingUnit;

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

    // 导出使用
    @TableField(select = false, exist = false)
    private Integer index;
}
