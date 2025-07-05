package com.ruoyi.process.dto;

import lombok.Data;

@Data
//要求、标书和合同评审的样品详情
public class SampleItemDto {

    //样品编号
    private String code;

    //样品型号
    private String model;

    //试验项目
    private String product;

    //检验依据
    private String standardMethodList;

    //备注
    private String remark;
}
