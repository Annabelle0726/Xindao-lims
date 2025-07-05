package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualityMonitorDetailsEvaluate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-20 星期五 9:08:38
 * Description:
 */
@Data
public class QualityMonitorDetailsEvaluateDto extends QualityMonitorDetailsEvaluate {
    @ApiModelProperty("批准时间")
    private String ratifyTimeStr;
}
