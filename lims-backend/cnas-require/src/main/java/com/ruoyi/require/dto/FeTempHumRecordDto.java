package com.ruoyi.require.dto;

import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.require.pojo.FeTempHumRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FeTempHumRecordDto extends FeTempHumRecord {

    @ApiModelProperty("下午记录员名称")
    private String afternoonRecorderUser;

    @ApiModelProperty("上午记录员名称")
    private String morningRecorderUser;

    @ApiModelProperty("检测天")
    private Integer month;

    @ApiModelProperty("下午时间")
    private String afternoonTimeStr;

    @ApiModelProperty("上午-时间")
    private String morningTestTimeStr;

    @ApiModelProperty("检测天")
    private Integer monthDay;

    @ApiModelProperty("登记员")
    private Integer registrarUserId;

    @ApiModelProperty("试验区域名称")
    private String testAreaName;

    @ApiModelProperty("上午记录员签名地址")
    private String morningRecorderUrl;

    @ApiModelProperty("下午记录员签名地址")
    private String afternoonRecorderUrl;


    @ApiModelProperty("上午记录员签名地址")
    private PictureRenderData morningRecorderUrlRender;

    @ApiModelProperty("下午记录员签名地址")
    private PictureRenderData afternoonRecorderUrlRender;


    @ApiModelProperty("最高温度")
    private Double temperatureHighest;

    @ApiModelProperty("最低温度")
    private Double temperatureLowest;

    @ApiModelProperty("最高湿度")
    private Double humidityHighest;

    @ApiModelProperty("最低湿度")
    private Double humidityLowest;

    @ApiModelProperty("是否是疑问数据")
    private Integer isIssue;

}
