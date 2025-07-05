package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualitySuperviseDetailsCorrect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/12/2
 */
@Data
public class QualitySuperviseDetailsCorrectDto extends QualitySuperviseDetailsCorrect {

    @ApiModelProperty("提出时间")
    private String raiseTimeString;

    @ApiModelProperty("原因分析时间")
    private String causeTimeString;

    @ApiModelProperty("纠正时间")
    private String correctTimeString;

    @ApiModelProperty("验证时间")
    private String validationTimeString;
}
