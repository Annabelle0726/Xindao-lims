package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 不符合项的分布详情
 * </p>
 *
 * @author 
 * @since 2024-11-15 09:53:33
 */
@Data
@TableName("cnas_inconsistent_distribution_detail")
@ApiModel(value = "InconsistentDistributionDetail对象", description = "不符合项的分布详情")
public class InconsistentDistributionDetail {

    @TableId(value = "distribution_detail_id", type = IdType.AUTO)
    private Integer distributionDetailId;

    @ApiModelProperty("主表id")
    private Integer distributionId;

    @ApiModelProperty("章节号")
    private String chapterNumber;

    @ApiModelProperty("要素")
    private String essentials;

    @ApiModelProperty("主任")
    private Integer director;

    @ApiModelProperty("技术负责人")
    private Integer technology;

    @ApiModelProperty("质量负责人")
    private Integer quality;

    @ApiModelProperty("综合室")
    private Integer comprehensive;

    @ApiModelProperty("试验室")
    private Integer testing;

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

    @ApiModelProperty("合计")
    @TableField(select = false,exist = false)
    private Integer total;

    @ApiModelProperty("占比")
    @TableField(select = false,exist = false)
    private BigDecimal proportion;
}
