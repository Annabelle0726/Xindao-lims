package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceExaminePlan;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/12/16
 */
@Data
public class DeviceExaminePlanDto extends DeviceExaminePlan {

    @ApiModelProperty("编制人")
    private String writeName;

    @ApiModelProperty("批准人")
    private String ratifyName;

    @ApiModelProperty("编制时间")
    private String writeTimeStr;

    @ApiModelProperty("批准时间")
    private String ratifyTimeStr;

    @ApiModelProperty("年度")
    private String year;

    private List<DeviceExaminePlanDetails> examinePlanDetailsList;
}
