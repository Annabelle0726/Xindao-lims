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
 * 仪器设备检定/校准计划表
 * </p>
 *
 * @author 
 * @since 2024-11-13 02:53:05
 */
@Getter
@Setter
@TableName("cnas_fe_calibration_schedule")
@ApiModel(value = "FeCalibrationSchedule对象", description = "仪器设备检定/校准计划表")
public class FeCalibrationSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("仪器名称")
    private String instrumentName;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("管理编号")
    private String managementNumber;

    @ApiModelProperty("技术指标")
    private String technicalIndicators;

    @ApiModelProperty("检定周期")
    private String verificationCyde;

    @ApiModelProperty("检定单位")
    private String verificationUnit;

    @ApiModelProperty("最近检定时间")
    private LocalDateTime recentlyTime;

    @ApiModelProperty("计划下次检定时间")
    private LocalDateTime nextTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("编制")
    private String organization;

    @ApiModelProperty("编制日期")
    private LocalDateTime organizationDate;

    @ApiModelProperty("批准")
    private String approve;

    @ApiModelProperty("批准日期")
    private LocalDateTime approveDate;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("序号id, 导出使用")
    @TableField(exist = false, select = false)
    private Integer index;
}
