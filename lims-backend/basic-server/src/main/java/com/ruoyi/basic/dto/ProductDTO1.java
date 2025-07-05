package com.ruoyi.basic.dto;

import com.ruoyi.basic.pojo.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/8/5
 */
@Data
public class ProductDTO1 extends Product {

    @ApiModelProperty(value = "零件号")
    private String partNo;
}
