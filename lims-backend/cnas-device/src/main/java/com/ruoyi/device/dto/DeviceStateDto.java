package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceStateDto extends DeviceState {
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "管理编号")
    private String managementNumber;

    @ApiModelProperty(value = "操作日期 yyyy-MM-dd")
    private String submitDateString;

    @ApiModelProperty("负责人审批日期 yyyy-MM-dd")
    private String departmentDateString;

    @ApiModelProperty("计量室审批日期 yyyy-MM-dd")
    private String measuringRoomDateString;

    @ApiModelProperty("批准日期 yyyy-MM-dd")
    private String approvalDateString;

    @ApiModelProperty(value = "设备类型")
    private String largeCategory;
}
