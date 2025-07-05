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
 * 设施和环境条件-设施和环境条件要求-电源稳定性
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:16:52
 */
@Getter
@Setter
@TableName("cnas_fe_power_stable")
@ApiModel(value = "FePowerStable对象", description = "设施和环境条件-设施和环境条件要求-电源稳定性")
public class FePowerStable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电源稳定性id")
    @TableId(value = "power_stable_id", type = IdType.AUTO)
    private Integer powerStableId;

    @ApiModelProperty("测试地点")
    private String testLocation;

    @ApiModelProperty("测试日期")
    private LocalDate testDate;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("结论")
    private String conclusion;

    @ApiModelProperty("检测者id")
    private Integer testerId;

    @ApiModelProperty("核查人id")
    private Integer checkerId;

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
