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
 * cnas设备使用记录表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 11:06:47
 */
@Getter
@Setter
@TableName("device_record")
@ApiModel(value = "DeviceRecord对象", description = "cnas设备使用记录表")
public class DeviceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("温度")
    private String temperature;

    @ApiModelProperty("湿度")
    private String humidity;

    @ApiModelProperty("使用前0异常1良好")
    private Integer useBefore;

    @ApiModelProperty("使用后0异常1良好")
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
