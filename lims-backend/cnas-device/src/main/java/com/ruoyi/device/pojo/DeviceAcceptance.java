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
 * 设备验收(装备)
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:14
 */
@Getter
@Setter
@TableName("device_acceptance")
@ApiModel(value = "DeviceAcceptance对象", description = "设备验收(装备)")
public class DeviceAcceptance {

    @TableId(value = "acceptance_id", type = IdType.AUTO)
    private Integer acceptanceId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("到货日期")
    private LocalDate arrivalDate;

    @ApiModelProperty("金额")
    private String goldAmount;

    @ApiModelProperty("维修单位")
    private String maintenanceunit;

    @ApiModelProperty("收设备主机和备份情况")
    private String spareParts;

    @ApiModelProperty("安装和调试情况")
    private String installationDebugging;

    @ApiModelProperty("验收情况")
    private String checkSituation;

    @ApiModelProperty("接收签字")
    private String receivingSignature;

    @ApiModelProperty("厂家代表")
    private String producer;

    @ApiModelProperty("接收人")
    private String recipient;

    @ApiModelProperty("接收时间")
    private String recipientDate;

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
