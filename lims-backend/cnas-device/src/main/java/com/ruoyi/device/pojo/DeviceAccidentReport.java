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
 * 设备事故报告单
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 06:31:12
 */
@Getter
@Setter
@TableName("device_accident_report")
@ApiModel(value = "DeviceAccidentReport对象", description = "设备事故报告单")
public class DeviceAccidentReport {

    @TableId(value = "accident_report_id", type = IdType.AUTO)
    private Integer accidentReportId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("时间")
    private LocalDateTime accidentDate;

    @ApiModelProperty("事故情况描述")
    private String descriptionOfAccident;

    @ApiModelProperty("报告人id")
    private Integer reportUserId;

    @ApiModelProperty("报告人")
    private String reportUser;

    @ApiModelProperty("报告人填写时间")
    private LocalDate reportDate;

    @ApiModelProperty("评估人意见")
    private String assessorOpinion;

    @ApiModelProperty("评估人id")
    private Integer assessorUserId;

    @ApiModelProperty("评估人")
    private String assessorUser;

    @ApiModelProperty("评估人填写时间")
    private LocalDate assessorDate;

    @ApiModelProperty("部门负责人意见")
    private String departmentHeadOpinion;

    @ApiModelProperty("部门负责人id")
    private Integer departmentHeadUserId;

    @ApiModelProperty("部门负责人")
    private String departmentHeadUser;

    @ApiModelProperty("部门负责人填写时间")
    private LocalDate departmentHeadDate;

    @ApiModelProperty("技术负责人意见")
    private String technicalDirectorOpinion;

    @ApiModelProperty("技术负责人id")
    private Integer technicalDirectorUserId;

    @ApiModelProperty("技术负责人")
    private String technicalDirectorUser;

    @ApiModelProperty("技术负责人填写时间")
    private LocalDate technicalDirectorDate;

    @ApiModelProperty("主任意见")
    private String directorHeadOpinion;

    @ApiModelProperty("主任id")
    private Integer directorHeadUserId;

    @ApiModelProperty("主任")
    private String directorHeadUser;

    @ApiModelProperty("主任填写时间")
    private LocalDate directorHeadDate;

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
    @ApiModelProperty("流程, 0:报告, 1评估, 2:部门负责人意见, 3:技术负责人意见, 4:主任意见")
    private Integer flowType;
}
