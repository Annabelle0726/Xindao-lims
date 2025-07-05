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
 * 设备量值溯源计划详情表
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:39
 */
@Getter
@Setter
@TableName("device_impower_details")
@ApiModel(value = "DeviceImpowerDetails对象", description = "设备量值溯源计划详情表")
public class DeviceImpowerDetails implements Serializable {


    @ApiModelProperty("设备量值溯源计划详情id")
    @TableId(value = "impower_detail_id", type = IdType.AUTO)
    private Integer impowerDetailId;

    @ApiModelProperty("设备量值溯源计划id")
    private Integer impowerId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("检测项目")
    private String inspectionItem;

    @ApiModelProperty("被授权人")
    private String delegatedUser;

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
