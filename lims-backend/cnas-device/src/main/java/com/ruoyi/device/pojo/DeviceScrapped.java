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
 * 设备报废申请表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 01:53:47
 */
@Getter
@Setter
@TableName("device_scrapped")
@ApiModel(value = "DeviceScrapped对象", description = "设备报废申请表")
public class DeviceScrapped {

    @TableId(value = "scrapped_id", type = IdType.AUTO)
    private Integer scrappedId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("配件")
    private String parts;

    @ApiModelProperty("报废理由")
    private String reasonsForScrap;

    @ApiModelProperty("申请人id")
    private Integer applicantUserId;

    @ApiModelProperty("申请人")
    private String applicantUser;

    @ApiModelProperty("申请时间")
    private LocalDate applicantDate;

    @ApiModelProperty("部门负责人意见")
    private String departmentHeadOpinion;

    @ApiModelProperty("部门负责人id")
    private Integer departmentHeadUserId;

    @ApiModelProperty("部门负责人")
    private String departmentHeadUser;

    @ApiModelProperty("部门负责人填写时间")
    private LocalDate departmentHeadDate;

    @ApiModelProperty("计量室意见")
    private String meteringRoomOpinion;

    @ApiModelProperty("计量室人id")
    private Integer meteringRoomUserId;

    @ApiModelProperty("计量室人")
    private String meteringRoomUser;

    @ApiModelProperty("计量室人填写时间")
    private LocalDate meteringRoomDate;

    @ApiModelProperty("批准人意见")
    private String approverOpinion;

    @ApiModelProperty("批准人id")
    private Integer approverUserId;

    @ApiModelProperty("批准人")
    private String approverUser;

    @ApiModelProperty("批准人填写时间")
    private LocalDate approverDate;

    @ApiModelProperty("是否结束,0: 未结束, 1:结束")
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
    @ApiModelProperty("流程, 0:报废申请, 1申请部门负责人意见, 2:计量室意见, 3:批准人")
    private Integer flowType;
}
