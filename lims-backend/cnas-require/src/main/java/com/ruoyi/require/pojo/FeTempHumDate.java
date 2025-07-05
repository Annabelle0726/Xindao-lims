package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度 区域 -父
 * </p>
 *
 * @author
 * @since 2024-11-09 11:02:18
 */
@Getter
@Setter
@TableName("cnas_fe_temp_hum_date")
@ApiModel(value = "FeTempHumDate对象", description = "设施和环境条件-设施和环境条件要求-温湿度 区域 -父")
public class FeTempHumDate {

    @ApiModelProperty("主键ID")
    @TableId(value = "date_id", type = IdType.AUTO)
    private Integer dateId;

    @ApiModelProperty("月度时间")
    private String monthDate;

    @ApiModelProperty("试验区域名称")
    private String testAreaName;

    @ApiModelProperty("增补信息")
    private String subjoin;

    @ApiModelProperty("登记员")
    private Integer registrarUserId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
}
