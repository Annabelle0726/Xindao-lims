package com.ruoyi.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备采集对象
 *
 * @Author zhuo
 * @Date 2024/12/3
 */
@Data
public class DeviceCollectionDto {

    @ApiModelProperty(value = "样品id")
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String entrustCode;

    @ApiModelProperty(value = "样品编号")
    private String sampleCode;

    @ApiModelProperty(value = "采集的检验项id")
    private List<Integer> itemIds;
}
