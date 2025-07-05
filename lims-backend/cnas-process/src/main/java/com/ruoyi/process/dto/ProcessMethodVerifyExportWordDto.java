package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.ProcessMethodVerify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-19 星期四 15:55:46
 * Description:
 */
@Data
public class ProcessMethodVerifyExportWordDto extends ProcessMethodVerify {
    @ApiModelProperty("(人)是否满足 1满足 0不满足")
    private String personIsSatisfiedStr;

    @ApiModelProperty("(机)是否满足")
    private String machineIsSatisfiedStr;

    @ApiModelProperty("(料)是否满足")
    private String materialIsSatisfiedStr;

    @ApiModelProperty("(法)是否满足")
    private String methodIsSatisfiedStr;

    @ApiModelProperty("(环)是否满足")
    private String environmentIsSatisfiedStr;

    @ApiModelProperty("(测量溯源性)是否满足")
    private String traceabilityIsSatisfiedStr;

    @ApiModelProperty("(样品管理需求)是否满足")
    private String managementIsSatisfiedStr;

    @ApiModelProperty("(其他)是否满足")
    private String otherIsSatisfiedStr;

    @ApiModelProperty("确认时间")
    private String confirmDateStr;
}
