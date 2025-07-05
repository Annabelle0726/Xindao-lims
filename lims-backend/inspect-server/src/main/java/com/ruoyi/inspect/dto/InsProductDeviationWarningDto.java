package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProductDeviationWarning;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/3/28
 */
@Data
public class InsProductDeviationWarningDto extends InsProductDeviationWarning {

    @ApiModelProperty("样品")
    private String sampleName;

    @ApiModelProperty("型号")
    private String sampleModel;

    @ApiModelProperty("检验项")
    private String inspectionItemName;

}
