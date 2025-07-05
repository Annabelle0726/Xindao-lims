package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.InternalCorrect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/19
 */
@Data
public class InternalCorrectDto extends InternalCorrect {

    @ApiModelProperty("提出时间")
    private String raiseTimeString;

    @ApiModelProperty("原因分析时间")
    private String causeTimeString;

    @ApiModelProperty("纠正时间")
    private String correctTimeString;

    @ApiModelProperty("验证时间")
    private String validationTimeString;

}
