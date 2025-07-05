package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 设备使用授权表
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:23
 */
@Getter
@Setter
@TableName("device_impower")
@ApiModel(value = "DeviceImpower对象", description = "设备使用授权表")
public class DeviceImpower implements Serializable {

    @ApiModelProperty("授权id")
    @TableId(value = "impower_id", type = IdType.AUTO)
    private Integer impowerId;

    @ApiModelProperty("计划年份")
    private String impowerYear;

    @ApiModelProperty("编制人id")
    private Integer compilerId;

    @ApiModelProperty("编制人")
    private String compiler;

    @ApiModelProperty("编制时间")
    private LocalDateTime datePreparation;

    @ApiModelProperty("审核状态，0未审核，1审核")
    private Integer status;

    @ApiModelProperty("授权id")
    private Integer auditId;

    @ApiModelProperty("授权人")
    private String audit;

    @ApiModelProperty("授权日期")
    private LocalDateTime auditDate;

    @ApiModelProperty("授权信息")
    private String auditRemark;

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
