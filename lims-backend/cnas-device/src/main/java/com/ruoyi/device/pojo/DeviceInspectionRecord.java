package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备点检记录
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:25:14
 */
@Getter
@Setter
@TableName("device_inspection_record")
@ApiModel(value = "DeviceInspectionRecord对象", description = "")
public class DeviceInspectionRecord implements Serializable {

    @ApiModelProperty("设备点检记录id")
    @TableId(value = "inspection_record_id", type = IdType.AUTO)
    private Integer inspectionRecordId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("测量范围")
    private String measurementScope;

    @ApiModelProperty("点检使用物质名称")
    private String materialName;

    @ApiModelProperty("点检使用物质规格型号")
    private String materialModel;

    @ApiModelProperty("点检使用物质管理编号")
    private String materialManagementNumber;

    @ApiModelProperty("点检使用物质精度等级")
    private String materialAccuracyGrade;

    @ApiModelProperty("温度")
    private String temperature;

    @ApiModelProperty("湿度")
    private String humidity;

    @ApiModelProperty("测试结论")
    private String testConclusion;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态（是否复核）0未复核，1复核")
    private Integer status;

    @ApiModelProperty("测试人")
    private String recorder;

    @ApiModelProperty("测试人id")
    private Integer recorderId;

    @ApiModelProperty("复核人")
    private String reviewer;

    @ApiModelProperty("复核人id")
    private Integer reviewerId;

    @ApiModelProperty("复核信息")
    private String reviewerRemark;

    @ApiModelProperty("测试日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime testDate;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("修改人id")
    private Integer updateUserId;
}
