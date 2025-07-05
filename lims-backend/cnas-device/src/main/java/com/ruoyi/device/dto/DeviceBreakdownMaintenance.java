package com.ruoyi.device.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备故障维修表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 04:50:57
 */
@Getter
@Setter
@TableName("device_breakdown_maintenance")
@ApiModel(value = "DeviceBreakdownMaintenance对象", description = "设备故障维修表")
public class DeviceBreakdownMaintenance {

    @TableId(value = "maintenance_id", type = IdType.AUTO)
    private Integer maintenanceId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("安装地址")
    private String location;

    @ApiModelProperty("损坏或故障情况")
    private String damageOrMalfunction;

    @ApiModelProperty("申请人id")
    private Integer applicantUserId;

    @ApiModelProperty("申请人")
    private String applicantUser;

    @ApiModelProperty("要求修复时间")
    private LocalDate repairDate;

    @ApiModelProperty("部门负责人意见")
    private String departmentHeadOpinion;

    @ApiModelProperty("部门负责人id")
    private Integer departmentHeadUserId;

    @ApiModelProperty("部门负责人")
    private String departmentHeadUser;

    @ApiModelProperty("部门负责人填写时间")
    private LocalDate departmentHeadDate;

    @ApiModelProperty("维修记事")
    private String maintenanceRecord;

    @ApiModelProperty("维修人")
    private String maintenanceUser;

    @ApiModelProperty("维修时间")
    private LocalDate maintenanceDate;

    @ApiModelProperty("是否结束,0 未结束, 1结束")
    private Integer isFinish;

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

    @TableField(exist = false,select = false)
    @ApiModelProperty("流程, 0:申请, 1申请部门负责人意见, 2:维修记事")
    private Integer flowType;
}
