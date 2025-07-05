package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhuo
 * @Date 2024/9/29
 */
@Configuration
@ConfigurationProperties(prefix = "ifs")
@Data
public class IfsProperties {

    /**
     * 域
     */
    private String contract;

    /**
     * 秘钥-get
     */
    private String contractKeyGet;


    /**
     * 秘钥-post
     */
    private String contractKeyPost;

    /**
     * 80
     */
    private String custorder;

    /**
     * 8008
     */
    private String custorderPort;

    /**
     * 8081
     */
    private String erpServices;
}
