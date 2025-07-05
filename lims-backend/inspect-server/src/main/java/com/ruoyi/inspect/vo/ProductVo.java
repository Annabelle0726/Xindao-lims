package com.ruoyi.inspect.vo;

import com.ruoyi.inspect.pojo.InsProduct;
import lombok.Data;

@Data
public class ProductVo  {

    //管色标
    private String bushColor;

    //光纤色标
    private String color;

    //光纤带编号
    private String code;

    //检验项目
    private InsProduct insProduct;


}
