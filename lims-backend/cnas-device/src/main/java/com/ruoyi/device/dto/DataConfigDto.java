package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DataConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataConfigDto {

    private List<DataConfig> dataConfigList;

    private Boolean isDevice;

    @ApiModelProperty("文件后缀")
    private String fileType;

    @ApiModelProperty("采集地址")
    private String collectUrl;

    @ApiModelProperty("存储地址")
    private String storageUrl;

    @ApiModelProperty("设备IP")
    private String ip;

    @ApiModelProperty("数采-委托字段")
    private String entrustCode;

    @ApiModelProperty("数采-样品字段")
    private String sampleCode;

    @ApiModelProperty("数采-db，mdb文件名称")
    private String dbFileName;

    @ApiModelProperty("设备ID")
    private Integer deviceId;
}
