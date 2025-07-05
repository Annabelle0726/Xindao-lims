package com.ruoyi.device.excel.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/12/20
 */
@Data
public class DeviceExaminePlanUpload {

    @ApiModelProperty("设备编号")
    private String deviceNumber;

    @ApiModelProperty("计划名称")
    private String deviceName;

    @ApiModelProperty("核查时间")
    private String checkTime;

    @ApiModelProperty("核查指标")
    private String checkIndex;

    @ApiModelProperty("核查方法")
    private String checkMethod;

    @ApiModelProperty("结果如何判定")
    private String howResults;

    @ApiModelProperty("核查责任人")
    private String checkChargerUser;

    @ApiModelProperty("备注")
    private String remark;

}
