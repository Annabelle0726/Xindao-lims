package com.ruoyi.inspect.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检测项分析结果数据
 *
 * @Author zhuo
 * @Date 2025/3/26
 */
@Data
public class InsProductAnalysisDto {

    @ApiModelProperty("检测项id")
    private Integer insProductId;

    @ApiModelProperty("检测结果")
    @TableField("`last_value`")
    private String lastValue;

    @ApiModelProperty("订单id")
    private Integer insOrderId;

    @ApiModelProperty("样品id")
    private Integer insSampleId;

}
