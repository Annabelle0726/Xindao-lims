package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceAccidentReport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-18 星期三 10:00:48
 * Description:
 */
@Data
public class DeviceAccidentReportDto extends DeviceAccidentReport {
    @ApiModelProperty("时间")
    private String accidentDateStr;

    @ApiModelProperty("报告人填写时间")
    private String reportDateStr;

    @ApiModelProperty("评估人填写时间")
    private String assessorDateStr;

    @ApiModelProperty("部门负责人填写时间")
    private String departmentHeadDateStr;

    @ApiModelProperty("技术负责人填写时间")
    private String technicalDirectorDateStr;

    @ApiModelProperty("主任填写时间")
    private String directorHeadDateStr;
}
