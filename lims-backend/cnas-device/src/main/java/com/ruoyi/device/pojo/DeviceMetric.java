package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("device_metrics")
public class DeviceMetric implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; //id
    private Integer deviceId;            // 设备ID
    private String measurementParameter; // 计量参数
    private String rangeOfMeasurement;   // 量程范围
    private String maxPermissibleError;  // 最大允许误差
    private String judgmentCriteria;     // 判定标准
    private String createdBy;           // 创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;      // 创建时间

    @ApiModelProperty("calibrate：校准；examine：核查")
    private String type; // 类型
}
