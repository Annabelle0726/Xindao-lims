package com.ruoyi.common.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {

    private Integer id;

    private String name;

    private Integer userId;

    private Integer fatherId;

    private List<DepartmentDto> children;
}
