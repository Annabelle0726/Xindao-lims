package com.ruoyi.device.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备停用/启用
 * </p>
 *
 * @author
 * @since 2024-09-26 09:51:40
 */
@Getter
@Setter
@TableName("device_state")
@ApiModel(value = "DeviceState对象", description = "设备停用/启用")
public class DeviceState implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备停用启用id")
    @TableId(value = "state_id", type = IdType.AUTO)
    private Integer stateId;

    @ApiModelProperty("流程编号")
    private String processNumber;

    @ApiModelProperty("0配件")
    private String accessoryPart;

    @ApiModelProperty("0设备状态")
    private String deviceStatus;

    @ApiModelProperty("0停用启用理由")
    private String reason;

    @ApiModelProperty("0下环节责任人")
    private String submitNextPesponsible;

    @ApiModelProperty("0操作人")
    private String submitOperatingPersonnel;

    @ApiModelProperty("0日期")
    private LocalDateTime submitDate;

    @ApiModelProperty("1部门负责人意见")
    private String departmentReviewOpinion;

    @ApiModelProperty("1下环节责任人")
    private String departmentNextPesponsible;

    @ApiModelProperty("1操作人")
    private String departmentOperatingPersonnel;

    @ApiModelProperty("1日期")
    private LocalDateTime departmentDate;

    @ApiModelProperty("2计量室意见")
    private String measuringRoomReviewOpinion;

    @ApiModelProperty("2下环节责任人")
    private String measuringRoomNextPesponsible;

    @ApiModelProperty("2操作人")
    private String measuringRoomOperatingPersonnel;

    @ApiModelProperty("2日期")
    private LocalDateTime measuringRoomDate;

    @ApiModelProperty("3批准意见")
    private String approvalOpinion;

    @ApiModelProperty("3下环节责任人")
    private String approvalNextPesponsible;

    @ApiModelProperty("3操作人")
    private String approvalOperatingPersonnel;

    @ApiModelProperty("3日期")
    private LocalDateTime approvalDate;

    @ApiModelProperty("当前状态")
    private String currentState;

    @ApiModelProperty("设备Id")
    private Integer deviceId;

    @ApiModelProperty("当前环节负责人")
    private String currentResponsible;

    @ApiModelProperty("提交人")
    @ExcelProperty(value = "提交人")
    private String createUser;

    @ApiModelProperty("提交日期")
    @ExcelProperty(value = "提交日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
