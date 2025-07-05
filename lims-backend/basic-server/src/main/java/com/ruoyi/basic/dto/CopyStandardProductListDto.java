package com.ruoyi.basic.dto;

import com.ruoyi.basic.pojo.StandardProductList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 标准检验项复制
 *
 * @Author zhuo
 * @Date 2024/10/31
 */
@Data
public class CopyStandardProductListDto {

    @ApiModelProperty("原本检验项信息")
    private List<StandardProductList> oldStandardProductList;

    @ApiModelProperty("需要对比的检验项信息")
    private List<StandardProductList> newStandardProductList;
}
