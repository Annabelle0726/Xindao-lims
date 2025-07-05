package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/29
 */
@Data
public class InsOrderUpdateDto {

    @ApiModelProperty("订单修改对象")
    private InsOrder insOrder;

    @ApiModelProperty("检测样品对象")
    private List<SampleProductDto> sampleProduct;
}
