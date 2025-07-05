package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度记录
 * </p>
 *
 * @author
 * @since 2024-11-07 04:28:52
 */
@Getter
@Setter
@TableName("cnas_fe_temp_hum_record")
@ApiModel(value = "FeTempHumRecord对象", description = "设施和环境条件-设施和环境条件要求-温湿度记录")
public class FeTempHumRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("温湿度记录")
    @TableId(value = "temp_hum_id", type = IdType.AUTO)
    private Integer tempHumId;

    @ApiModelProperty("记录日期")
    private LocalDate recordDate;

    @ApiModelProperty("上午-时间")
    private LocalTime morningTestTime;

    @ApiModelProperty("上午温度")
    private String morningTemp;

    @ApiModelProperty("上午湿度")
    private String morningHum;

    @ApiModelProperty("上午记录员")
    private Integer morningRecorderId;

    @ApiModelProperty("下午时间")
    private LocalTime afternoonTime;

    @ApiModelProperty("下午温度")
    private String afternoonTemp;

    @ApiModelProperty("下午湿度")
    private String afternoonHum;

    @ApiModelProperty("下午记录员")
    private Integer afternoonRecorderId;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("温度循环主表id")
    private Integer dateId;
}
