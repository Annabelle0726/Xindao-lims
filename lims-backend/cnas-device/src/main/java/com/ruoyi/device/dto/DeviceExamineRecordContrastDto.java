package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceExamineRecordContrast;
import com.ruoyi.device.pojo.DeviceExamineRecordContrastDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/12/16
 */
@Data
public class DeviceExamineRecordContrastDto extends DeviceExamineRecordContrast {

    @ApiModelProperty("核查记录对比详情")
    private List<DeviceExamineRecordContrastDetails> recordContrastDetailsList;


    @ApiModelProperty("A设备名称")
    private String aDeviceName;

    @ApiModelProperty("A设备编号")
    private String aDeviceNumber;

    @ApiModelProperty("b设备名称")
    private String bDeviceName;

    @ApiModelProperty("b设备编号")
    private String bDeviceNumber;

    @ApiModelProperty("c设备名称")
    private String cDeviceName;

    @ApiModelProperty("c设备编号")
    private String cDeviceNumber;

    @ApiModelProperty("核查日期")
    private String checkerTimeStr;

    @ApiModelProperty("审核日期")
    private String reviewTimeStr;

    @ApiModelProperty("实验室")
    private String labName;
}
