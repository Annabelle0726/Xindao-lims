package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceScrapped;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 18:34:17
 * Description:
 */
@Data
public class DeviceScrappedDto extends DeviceScrapped {

    @ApiModelProperty("申请时间")
    private String applicantDateStr;

    @ApiModelProperty("部门负责人填写时间")
    private String departmentHeadDateStr;

    @ApiModelProperty("计量室人填写时间")
    private String meteringRoomDateStr;

    @ApiModelProperty("批准人填写时间")
    private String approverDateStr;
}
