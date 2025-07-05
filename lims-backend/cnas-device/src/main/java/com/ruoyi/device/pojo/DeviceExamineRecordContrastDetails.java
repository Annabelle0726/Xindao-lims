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
 * 设备核查记录对比详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:57
 */
@Getter
@Setter
@TableName("device_examine_record_contrast_details")
@ApiModel(value = "DeviceExamineRecordContrastDetails对象", description = "设备核查记录对比详情表")
public class DeviceExamineRecordContrastDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_contrast_details_id", type = IdType.AUTO)
    private Integer recordContrastDetailsId;

    @ApiModelProperty("设备核查详情id")
    private Integer recordContrastId;

    @ApiModelProperty("核查项目")
    private String checkItems;

    @ApiModelProperty("a仪器示值")
    private String indicationA;

    @ApiModelProperty("b仪器示值")
    private String indicationB;

    @ApiModelProperty("c仪器示值")
    private String indicationC;

    @ApiModelProperty("差值")
    private String dValue;

    @ApiModelProperty("偏差")
    private String deviation;

    @ApiModelProperty("判定")
    private String determine;

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
