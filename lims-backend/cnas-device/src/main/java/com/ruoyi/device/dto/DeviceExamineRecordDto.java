package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceExamineRecord;
import com.ruoyi.device.pojo.DeviceExamineRecordDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/12/16
 */
@Data
public class DeviceExamineRecordDto extends DeviceExamineRecord {

    @ApiModelProperty("核查记录详情")
    private List<DeviceExamineRecordDetail> recordDetailList;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceNumber;

    @ApiModelProperty("最后一次修改日期")
    private String updateTimeStr;
}
