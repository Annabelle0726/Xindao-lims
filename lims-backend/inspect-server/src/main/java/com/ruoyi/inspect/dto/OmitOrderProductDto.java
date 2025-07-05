package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/12/12
 */
@Data
public class OmitOrderProductDto {
    @ApiModelProperty("需要添加的样品Id")
    private Integer insSampleId;


    @ApiModelProperty("需要绑定的检验项")
    private List<InsProduct> insProductBindingList;
}
