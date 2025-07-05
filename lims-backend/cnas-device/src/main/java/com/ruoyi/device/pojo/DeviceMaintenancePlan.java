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
 * 设备保养计划表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:10:52
 */
@Getter
@Setter
@TableName("device_maintenance_plan")
@ApiModel(value = "DeviceMaintenancePlan对象", description = "设备保养计划表")
public class DeviceMaintenancePlan implements Serializable {

    @ApiModelProperty("设备保养计划id")
    @TableId(value = "maintenance_plan_id", type = IdType.AUTO)
    private Integer maintenancePlanId;

    @ApiModelProperty("编制人")
    private String compiler;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("计划年份")
    private String planYear;

    @ApiModelProperty("编制人id")
    private Integer compilerId;

    @ApiModelProperty("编制日期")
    private LocalDateTime datePreparation;

    @ApiModelProperty("审核状态，0未审核，1审核")
    private Integer status;

    @ApiModelProperty("审核人id")
    private Integer auditId;

    @ApiModelProperty("审核人")
    private String audit;

    @ApiModelProperty("审核日期")
    private LocalDateTime auditDate;

    @ApiModelProperty("审核信息")
    private String auditRemark;

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
