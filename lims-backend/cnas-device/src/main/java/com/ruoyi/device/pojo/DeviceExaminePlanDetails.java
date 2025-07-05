package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备核查计划详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:16
 */
@Getter
@Setter
@TableName("device_examine_plan_details")
@ApiModel(value = "DeviceExaminePlanDetails对象", description = "设备核查计划详情表")
public class DeviceExaminePlanDetails {

    @TableId(value = "plan_details_id", type = IdType.AUTO)
    private Integer planDetailsId;

    @ApiModelProperty("主表id")
    private Integer planId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("设备编号")
    private String deviceNumber;

    @ApiModelProperty("计划名称")
    private String deviceName;

    @ApiModelProperty("核查时间")
    private String checkTime;

    @ApiModelProperty("核查指标")
    private String checkIndex;

    @ApiModelProperty("核查方法")
    private String checkMethod;

    @ApiModelProperty("结果如何判定")
    private String howResults;

    @ApiModelProperty("核查责任人id")
    private Integer checkChargerUserId;

    @ApiModelProperty("核查责任人")
    private String checkChargerUser;

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
