package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 人事系统api
 *
 * @Author zhuo
 * @Date 2025/3/16
 */
@Configuration
@ConfigurationProperties(prefix = "personnel")
@Data
public class PersonnelProperties {
    /**
     * 正式地址
     * */
    private String code;
    /**
     * 装备人事正式库
     * */
    private String appId;
    private String appSecret;

    private String companies;

    private String simple;

    /**
     * 人员密码获取
     * */
    private String password;

    private String department;

    private String person;
}
