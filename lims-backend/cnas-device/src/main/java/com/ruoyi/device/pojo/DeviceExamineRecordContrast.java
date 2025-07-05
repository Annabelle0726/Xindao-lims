package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备核查记录对比表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:43
 */
@Getter
@Setter
@TableName("device_examine_record_contrast")
@ApiModel(value = "DeviceExamineRecordContrast对象", description = "设备核查记录对比表")
public class DeviceExamineRecordContrast implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_contrast_id", type = IdType.AUTO)
    private Integer recordContrastId;

    @ApiModelProperty("核查方式")
    private String checkMethod;

    @ApiModelProperty("设备核查详情id")
    private Integer planDetailsId;

    @ApiModelProperty("设备idA")
    private Integer aDeviceId;

    @ApiModelProperty("设备idb")
    private Integer bDeviceId;

    @ApiModelProperty("设备idc")
    private Integer cDeviceId;

    @ApiModelProperty("范围不确定度A")
    private String aRangeUncertainty;

    @ApiModelProperty("范围不确定度b")
    private String bRangeUncertainty;

    @ApiModelProperty("范围不确定度c")
    private String cRangeUncertainty;

    @ApiModelProperty("综合判定")
    private String judgment;

    @ApiModelProperty("核查人id")
    private Integer checkerUserId;

    @ApiModelProperty("核查人")
    private String checkerUser;

    @ApiModelProperty("核查日期")
    private LocalDateTime checkerTime;

    @ApiModelProperty("审核人id")
    private Integer reviewUserId;

    @ApiModelProperty("审核人")
    private String reviewUser;

    @ApiModelProperty("审核状态0,不通过, 1通过")
    private Integer reviewStatus;

    @ApiModelProperty("审核备注")
    private String reviewRemark;

    @ApiModelProperty("审核日期")
    private LocalDateTime reviewTime;

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
