package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualityMonitor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/6
 */
@Data
public class QualityMonitorDto extends QualityMonitor {

    @ApiModelProperty("编制人")
    private String writeName;

    @ApiModelProperty("审核人")
    private String examineName;

    @ApiModelProperty("批准人")
    private String ratifyName;
}
