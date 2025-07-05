package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/29
 */
@Data
public class InsProductBindingDto {
    @ApiModelProperty("需要绑定的id")
    private Integer insProductId;


    @ApiModelProperty("需要绑定的检验项")
    private List<InsProduct> insProductBindingList;
}
