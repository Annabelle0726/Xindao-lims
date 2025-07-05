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
 * 设备保养计划详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:11:46
 */
@Getter
@Setter
@TableName("device_maintenance_plan_details")
@ApiModel(value = "DeviceMaintenancePlanDetails对象", description = "设备保养计划详情表")
public class DeviceMaintenancePlanDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("保养计划详情id")
    @TableId(value = "maintenance_plan_detail_id", type = IdType.AUTO)
    private Integer maintenancePlanDetailId;

    @ApiModelProperty("保养计划id")
    private Integer maintenancePlanId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("保养关键部位")
    private String maintenanceSite;

    @ApiModelProperty("保养内容")
    private String maintenanceContent;

    @ApiModelProperty("保养周期")
    private String maintenanceIntervals;

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
