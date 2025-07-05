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
 * 设备核查记录详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:15:11
 */
@Getter
@Setter
@TableName("device_examine_record_detail")
@ApiModel(value = "DeviceExamineRecordDetail对象", description = "设备核查记录详情表")
public class DeviceExamineRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_detail_id", type = IdType.AUTO)
    private Integer recordDetailId;

    @ApiModelProperty("设设备核查记录id")
    private Integer recordId;

    @ApiModelProperty("测试点")
    private String testPoint;

    @ApiModelProperty("内容值1")
    private String dataValue1;

    @ApiModelProperty("内容值2")
    private String dataValue2;

    @ApiModelProperty("内容值3")
    private String dataValue3;

    @ApiModelProperty("内容值4")
    private String dataValue4;

    @ApiModelProperty("内容值5")
    private String dataValue5;

    @ApiModelProperty("内容值6")
    private String dataValue6;

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
