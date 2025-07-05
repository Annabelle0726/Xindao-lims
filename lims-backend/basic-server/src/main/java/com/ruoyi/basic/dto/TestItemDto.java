package com.ruoyi.basic.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestItemDto {

    private Integer id;

    private String name;

    private List<ProductDto> children;

}
