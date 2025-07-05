package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备校准计划详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:29
 */
@Getter
@Setter
@TableName("device_calibration_plan_detail")
@ApiModel(value = "DeviceCalibrationPlanDetail对象", description = "设备校准计划详情表")
public class DeviceCalibrationPlanDetail{

    @TableId(value = "plan_detail_id", type = IdType.AUTO)
    private Integer planDetailId;

    @ApiModelProperty("计划主表id")
    private Integer planId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("设备名称及型号")
    private String deviceName;

    @ApiModelProperty("设备数量")
    private String deviceAmount;

    @ApiModelProperty("仪器编号")
    private String deviceNumber;

    @ApiModelProperty("检定单位")
    private String verificationUnit;

    @ApiModelProperty("检定周期")
    private String verificationCycles;

    @ApiModelProperty("最近检定时间")
    private LocalDate lastDate;

    @ApiModelProperty("本年计划校准时间")
    private LocalDate planDate;

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
