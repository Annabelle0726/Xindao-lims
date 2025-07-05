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
 * 设备核查记录表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:28
 */
@Getter
@Setter
@TableName("device_examine_record")
@ApiModel(value = "DeviceExamineRecord对象", description = "设备核查记录表")
public class DeviceExamineRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty("设备核查详情id")
    private Integer planDetailsId;

    @ApiModelProperty("精度等级")
    private String accuracyGrade;

    @ApiModelProperty("使用物质名称")
    private String materialName;

    @ApiModelProperty("使用物质管理编号")
    private String materialNumber;

    @ApiModelProperty("使用物质精度/不确定度")
    private String materialAccuracyUncertainty;

    @ApiModelProperty("使用物质规格型号")
    private String materialModel;

    @ApiModelProperty("使用物质核查方式")
    private String materialCheckMethod;

    @ApiModelProperty("使用物质核查项目")
    private String materialCheckItems;

    @ApiModelProperty("温度")
    private String temperature;

    @ApiModelProperty("湿度")
    private String humidity;

    @ApiModelProperty("判定")
    private String determine;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("核查人id")
    private Integer checkerUserId;

    @ApiModelProperty("核查人")
    private String checkerUser;

    @ApiModelProperty("复核人id")
    private Integer reviewUserId;

    @ApiModelProperty("复核人")
    private String reviewUser;

    @ApiModelProperty("审核状态0,不通过, 1通过")
    private Integer reviewStatus;

    @ApiModelProperty("审核备注")
    private String reviewRemark;

    @ApiModelProperty("测试点1")
    private String dataValue1;
    @ApiModelProperty("测试点2")
    private String dataValue2;
    @ApiModelProperty("测试点3")
    private String dataValue3;
    @ApiModelProperty("测试点4")
    private String dataValue4;
    @ApiModelProperty("测试点5")
    private String dataValue5;
    @ApiModelProperty("测试点6")
    private String dataValue6;

    @ApiModelProperty("最大偏差1")
    private String maximun1;
    @ApiModelProperty("最大偏差2")
    private String maximun2;
    @ApiModelProperty("最大偏差3")
    private String maximun3;
    @ApiModelProperty("最大偏差4")
    private String maximun4;
    @ApiModelProperty("最大偏差5")
    private String maximun5;
    @ApiModelProperty("最大偏差6")
    private String maximun6;

    @ApiModelProperty("相对偏差1")
    private String relative1;
    @ApiModelProperty("相对偏差2")
    private String relative2;
    @ApiModelProperty("相对偏差3")
    private String relative3;
    @ApiModelProperty("相对偏差4")
    private String relative4;
    @ApiModelProperty("相对偏差5")
    private String relative5;
    @ApiModelProperty("相对偏差6")
    private String relative6;

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
