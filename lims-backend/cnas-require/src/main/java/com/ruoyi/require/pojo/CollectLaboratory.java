package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 温湿度采集实验室绑定
 *
 * @author zhuo
 * @since 2024-12-18
 */
@Data
@TableName("collect_laboratory")
public class CollectLaboratory {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("实验室")
    private String laboratory;

    @ApiModelProperty("温湿度设备编号")
    private String deviceCode;

    @ApiModelProperty("最高温度")
    private Double temperatureHighest;

    @ApiModelProperty("最低温度")
    private Double temperatureLowest;

    @ApiModelProperty("最高湿度")
    private Double humidityHighest;

    @ApiModelProperty("最低湿度")
    private Double humidityLowest;

}

