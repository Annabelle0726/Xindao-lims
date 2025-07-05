package com.ruoyi.inspect.vo;

import com.ruoyi.inspect.pojo.InsSample;
import lombok.Data;

@Data
public class SampleVo extends InsSample {
    //试验方法
    private  String methodName;
}
