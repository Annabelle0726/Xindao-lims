package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验任务检验项查询条件
 *
 * @Author zhuo
 * @Date 2024/11/29
 */
@Data
public class InsOrderPlanProductDto {

    @ApiModelProperty("样品id")
    private Integer id;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("试验室")
    private String laboratory;

    @ApiModelProperty("电缆配置标识")
    private String cableTag;

    @ApiModelProperty("原材料批次标识")
    private String rawMaterialTag;

    @ApiModelProperty("成品下单重复标识")
    private String repetitionTag;

    @ApiModelProperty("不合格复测重复次数")
    private String retestTag;

}
