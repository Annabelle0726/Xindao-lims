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
 * 利用外部设备申请表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 10:28:43
 */
@Getter
@Setter
@TableName("device_external_apply")
@ApiModel(value = "DeviceExternalApply对象", description = "利用外部设备申请表")
public class DeviceExternalApply{

    @TableId(value = "external_apply_id", type = IdType.AUTO)
    private Integer externalApplyId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("仪器名称")
    private String deviceName;

    @ApiModelProperty("仪器型号")
    private String deviceModel;

    @ApiModelProperty("配件")
    private String parts;

    @ApiModelProperty("对方仪器编号")
    private String instrumentNumber;

    @ApiModelProperty("技术指标")
    private String technicalIndex;

    @ApiModelProperty("技术要求")
    private String technicalRequirements;

    @ApiModelProperty("利用原因")
    private String useReason;

    @ApiModelProperty("0申请人id")
    private Integer applicantUserId;

    @ApiModelProperty("0申请人")
    private String applicantUser;

    @ApiModelProperty("0申请时间")
    private LocalDate applicantDate;

    @ApiModelProperty("1部门负责人意见")
    private String departmentHeadOpinion;

    @ApiModelProperty("1部门负责人id")
    private Integer departmentHeadUserId;

    @ApiModelProperty("1部门负责人")
    private String departmentHeadUser;

    @ApiModelProperty("1部门负责人填写时间")
    private LocalDate departmentHeadDate;

    @ApiModelProperty("2计量室意见")
    private String meteringRoomOpinion;

    @ApiModelProperty("2计量室人id")
    private Integer meteringRoomUserId;

    @ApiModelProperty("2计量室人")
    private String meteringRoomUser;

    @ApiModelProperty("2计量室人填写时间")
    private LocalDate meteringRoomDate;

    @ApiModelProperty("3批准人意见")
    private String approverOpinion;

    @ApiModelProperty("3批准人id")
    private Integer approverUserId;

    @ApiModelProperty("3批准人")
    private String approverUser;

    @ApiModelProperty("3批准人填写时间")
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
    @ApiModelProperty("流程, 0:申请, 1申请部门负责人意见, 2:计量室意见, 3:批准人")
    private Integer flowType;
}
