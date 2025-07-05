package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 下单对象
 *
 * @Author zhuo
 * @Date 2025/2/19
 */
@Data
public class InsPlaceOrderDto{

    @ApiModelProperty("样品")
    private List<SampleProductDto> sampleList;

    @ApiModelProperty("订单信息")
    private InsOrder insOrder;

    @ApiModelProperty("铜单丝订单")
    private CopperInsOrderDto copperInsOrder;

}
