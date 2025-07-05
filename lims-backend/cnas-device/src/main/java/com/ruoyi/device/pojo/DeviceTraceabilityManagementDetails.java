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
 * 设备量值溯源计划详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:58
 */
@Getter
@Setter
@TableName("device_traceability_management_details")
@ApiModel(value = "DeviceTraceabilityManagementDetails对象", description = "设备量值溯源计划详情表")
public class DeviceTraceabilityManagementDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备量值溯源计划详情id")
    @TableId(value = "traceability_management_detail_id", type = IdType.AUTO)
    private Integer traceabilityManagementDetailId;

    @ApiModelProperty("设备量值溯源计划id")
    private Integer traceabilityManagementId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("技术指标参数")
    private String technicalIndexParameters;

    @ApiModelProperty("技术指标要求")
    private String technicalRequirements;

    @ApiModelProperty("检定周期")
    private String verificationCycle;

    @ApiModelProperty("检定单位")
    private String verificationUnit;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
