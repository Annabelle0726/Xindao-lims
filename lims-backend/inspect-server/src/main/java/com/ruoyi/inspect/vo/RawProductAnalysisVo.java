package com.ruoyi.inspect.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 项检分析返回对象
 *
 * @Author zhuo
 * @Date 2024/10/17
 */
@Data
public class RawProductAnalysisVo {

    @ApiModelProperty("检验项名称")
    private List<String> itemNames;

    @ApiModelProperty("检验项内容")
    private List<Map<String, Object>> productList;
}
