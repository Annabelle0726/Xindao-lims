package com.ruoyi.inspect.dto;

import com.ruoyi.basic.dto.ModelDto;
import com.ruoyi.basic.dto.SampleDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/7/31
 */
@Data
public class RawMaterialStandardTreeDto {

    @ApiModelProperty("树名称")
    private String treeName;

    @ApiModelProperty("树名称")
    private String code;

    @ApiModelProperty("树名称")
    private String label;

    @ApiModelProperty("树名称")
    private String value;

    @ApiModelProperty("产品")
    private List<SampleDto> children1;

    @ApiModelProperty("型号")
    private List<ModelDto> children2;

}
