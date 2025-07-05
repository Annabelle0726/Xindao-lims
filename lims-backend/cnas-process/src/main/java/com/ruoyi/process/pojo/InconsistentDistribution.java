package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 不符合项的分布
 * </p>
 *
 * @author
 * @since 2024-11-15 09:53:20
 */

@Data
@TableName("cnas_inconsistent_distribution")
@ApiModel(value = "InconsistentDistribution对象", description = "不符合项的分布")
public class InconsistentDistribution {

    @TableId(value = "distribution_id", type = IdType.AUTO)
    private Integer distributionId;

    @ApiModelProperty("年份")
    private String distributionYear;

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
