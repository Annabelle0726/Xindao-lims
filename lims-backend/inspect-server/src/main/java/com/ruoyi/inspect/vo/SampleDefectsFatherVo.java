package com.ruoyi.inspect.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleDefectsFatherVo {
    private Integer id;//样品id
    private String sample;//样品名称

    private List<SampleDefectsChildrenVo> children;//子类
}
