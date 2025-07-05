package com.ruoyi.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/8/22
 */
@Data
public class ResetTreeDragDTO {
    @ApiModelProperty(value = "开始索引")
    private String beginIndex;

    @ApiModelProperty(value = "结束索引")
    private String endIndex;

    @ApiModelProperty(value = "标准编号id")
    private String methodId;

    @ApiModelProperty(value = "数")
    private String tree;

    @ApiModelProperty(value = "检验项行id")
    private String productionId;
}
