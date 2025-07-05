package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备量值溯源计划表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:44
 */
@Getter
@Setter
@TableName("device_traceability_management")
@ApiModel(value = "DeviceTraceabilityManagement对象", description = "设备量值溯源计划表")
public class DeviceTraceabilityManagement{

    @ApiModelProperty("设备量值溯源计划id")
    @TableId(value = "traceability_management_id", type = IdType.AUTO)
    private Integer traceabilityManagementId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("计划年份")
    private String planYear;

    @ApiModelProperty("编制人id")
    private Integer compilerId;

    @ApiModelProperty("编制人")
    private String compiler;

    @ApiModelProperty("编制时间")
    private LocalDateTime datePreparation;

    @ApiModelProperty("审核状态，0未审核，1审核")
    private Integer status;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("审核人id")
    private Integer auditId;

    @ApiModelProperty("审核人")
    private String audit;

    @ApiModelProperty("审核日期")
    private LocalDateTime auditDate;

    @ApiModelProperty("审核信息")
    private String auditRemark;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
