package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:15:57
 */
@Getter
@Setter
@TableName("cnas_fe_illumination")
@ApiModel(value = "FeIllumination对象", description = "设施和环境条件-设施和环境条件要求-照度记录表")
public class FeIllumination implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("照度记录表id")
    @TableId(value = "intensity_illumination_id", type = IdType.AUTO)
    private Integer intensityIlluminationId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("结论")
    private String conclusion;

    @ApiModelProperty("检测人")
    private Integer testerId;

    @ApiModelProperty("核查人")
    private Integer checkerId;

    @ApiModelProperty("检测日期")
    private LocalDate testDate;

    @ApiModelProperty("校准日期")
    private LocalDateTime lastCalibrationDate;

    @ApiModelProperty("下次校准日期")
    private LocalDateTime nextCalibrationDate;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
