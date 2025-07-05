package com.ruoyi.basic.dto;

import lombok.Data;

import java.util.List;

@Data
public class FactoryDto {

    private String code = "[1]";

    private String label;

    private String value;

    private List<LaboratoryDto> children;

}
