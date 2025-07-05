package com.ruoyi.device.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.deepoove.poi.data.PictureRenderData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备维护添加维护记录表
 *
 * @author makejava
 * @since 2025-04-17 11:28:56
 */
@Data
@TableName("device_maintenance")
public class DeviceMaintenance {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("维护日期")
    private LocalDate maintenanceDate;

    @ApiModelProperty("维护内容")
    private String maintenanceContent;

    @ApiModelProperty("维护人id")
    private Integer maintenanceUserId;

    @ApiModelProperty("维护人")
    private String maintenanceUserName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(select = false, exist = false)
    @ApiModelProperty("维护人图片")
    private PictureRenderData maintenanceUserUrlRender;


}

