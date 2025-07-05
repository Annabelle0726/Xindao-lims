package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsProduct;
import lombok.Data;

import java.util.List;

@Data
public class InsulatingDto {

    // 方法id
    private Integer standardMethodListId;

    // 芯数
    private List<String> num;

    // 检验项
    private List<InsProduct> insProduct;

}
