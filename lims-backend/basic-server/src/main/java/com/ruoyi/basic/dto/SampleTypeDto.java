package com.ruoyi.basic.dto;

import lombok.Data;

import java.util.List;

@Data
public class SampleTypeDto {

    private String code = "[3]";

    // 检测对象id
    private Integer sampleTypeId;

    // 检测对象排序
    private Integer sort;

    private String label;

    private String value;

    private String partNo;

    // 对象英文
    private String sampleTypeEn;

    private List<SampleDto> children;

}
