package com.ruoyi.web.controller.api;

import lombok.Data;

/**
 * 公司信息
 */
@Data
public class Company {

    private String companyId;

    private String companyName;

    private String parentCompanyId;

    private String description;

    private String status;
}
