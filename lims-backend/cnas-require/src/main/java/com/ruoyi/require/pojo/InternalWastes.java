package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 安全内务三废登记
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:27
 */
@Data
@TableName("cnas_internal_wastes")
@ApiModel(value = "InternalWastes对象", description = "安全内务三废登记")
public class InternalWastes {

    @TableId(value = "wastes_id", type = IdType.AUTO)
    private Integer wastesId;

    @ApiModelProperty("备注")
    private String remark;

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
