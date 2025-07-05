package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备校准计划主表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:17
 */
@Getter
@Setter
@TableName("device_calibration_plan")
@ApiModel(value = "DeviceCalibrationPlan对象", description = "设备校准计划主表")
public class DeviceCalibrationPlan{

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("计划年份")
    private String planYear;

    @ApiModelProperty("编制人")
    private Integer writeUserId;

    @ApiModelProperty("编制时间")
    private LocalDateTime writeTime;

    @ApiModelProperty("批准人")
    private Integer ratifyUserId;

    @ApiModelProperty("批准时间")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态,0 不通过, 1 通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准信息")
    private String ratifyRemark;

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
