package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsSample;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SampleProductDto extends InsSample {

    @ApiModelProperty("检验项目")
    private List<InsProduct> insProduct;

    @ApiModelProperty("子样品配置")
    private List<SampleProductDto> childSampleList;

    @ApiModelProperty("电缆配置对象")
    private InsulatingDto insulating;

    @ApiModelProperty("辅助线芯配置")
    private InsulatingDto auxiliaryWireCore;

    @ApiModelProperty("检验人")
    private String checkName;

}
