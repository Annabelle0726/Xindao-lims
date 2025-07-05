package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductResultDto2 extends InsProduct {

    @ApiModelProperty("检验设备")
    private String equipValue;

    @ApiModelProperty("检验人")
    private String updateUserName;

    @ApiModelProperty("委托编号")
    private String entrustCode;

}
