package com.ruoyi.basic.dto;

import lombok.Data;

import java.util.List;

@Data
public class LaboratoryDto {

    private String code = "[2]";

    private String label;

    private String value;

    private List<SampleTypeDto> children;

}
