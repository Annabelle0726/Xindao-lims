package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备校准 - 校准记录 - 校准条目
 * </p>
 *
 * @author
 * @since 2024-09-27 10:20:11
 */
@Getter
@Setter
@TableName("device_metrics_copy")
@ApiModel(value = "DeviceMetricsCopy对象", description = "设备校准 - 校准记录 - 校准条目")
public class DeviceMetricsCopy implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("设备校准 - 校准记录id")
    private Integer deviceMetricsId;

    @ApiModelProperty("计量参数")
    private String measurementParameter;

    @ApiModelProperty("量程范围")
    private String rangeOfMeasurement;

    @ApiModelProperty("最大允许误差")
    private String maxPermissibleError;

    @ApiModelProperty("判定标准")
    private String judgmentCriteria;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime creationTime;

    @ApiModelProperty("是否校准")
    private String isCalibration;

    @ApiModelProperty("判定结果")
    private String result;

    @ApiModelProperty("单项结果说明")
    private String singleResultStatement;

    @ApiModelProperty("calibrate：校准；examine：核查")
    private String type;
}
