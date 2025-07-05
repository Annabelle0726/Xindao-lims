package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * cnas设备使用记录表(DeviceRecord)$desc
 *
 * @author makejava
 * @since 2024-12-21 11:11:01
 */
@Data
@TableName("device_record")
public class InsOrderDeviceRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("温度")
    private String temperature;

    @ApiModelProperty("湿度")
    private String humidity;

    @ApiModelProperty("使用前0代表不正常1代表正常")
    private Integer useBefore;

    @ApiModelProperty("使用后0代表不正常1代表正常")
    private Integer useAfter;

    @ApiModelProperty("异常情况")
    private String abnormal;

    @ApiModelProperty("使用人id")
    private Integer usePersonId;

    @ApiModelProperty("使用人")
    private String usePerson;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("使用开始日期")
    private LocalDateTime useStartDate;

    @ApiModelProperty("使用结束日期")
    private LocalDateTime useEndDate;

}

