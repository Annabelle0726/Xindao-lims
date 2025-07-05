package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 温湿度采集信息
 *
 * @author zhuo
 * @since 2024-12-18
 */
@Data
@TableName("collect_temperature_humidity")
public class CollectTemperatureHumidity {

    @TableId(type = IdType.AUTO)
    private Integer superviseId;
    //7801温度
    private Double code7801Temperature;
    //7801湿度
    private Double code7801Humidity;
    //7802温度
    private Double code7802Temperature;
    //7802湿度
    private Double code7802Humidity;
    //7803温度
    private Double code7803Temperature;
    //7803湿度
    private Double code7803Humidity;
    //7804温度
    private Double code7804Temperature;
    //7804湿度
    private Double code7804Humidity;
    //7805温度
    private Double code7805Temperature;
    //7805湿度
    private Double code7805Humidity;
    //7806温度
    private Double code7806Temperature;
    //7806湿度
    private Double code7806Humidity;
    //7807温度
    private Double code7807Temperature;
    //7807湿度
    private Double code7807Humidity;
    //7808温度
    private Double code7808Temperature;
    //7808湿度
    private Double code7808Humidity;
    //7809温度
    private Double code7809Temperature;
    //7809湿度
    private Double code7809Humidity;
    //7810温度
    private Double code7810Temperature;
    //7810湿度
    private Double code7810Humidity;
    //采集时间
    private Date collectTime;

}

