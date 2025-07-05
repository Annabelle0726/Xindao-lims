package com.ruoyi.web.controller.api;

import lombok.Data;

import java.util.List;

/**
 * 人事信息
 */
@Data
public class PersonDto {

    private List<Person> person;

    private List<Company> company;

}
