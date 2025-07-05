package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * zhuo
 */

@Configuration
@Component
@ConfigurationProperties(prefix = "sso")
@Data
public class SsoBean {

    /**
     * 单点登录应用id
     */
    private String clientId;

    /**
     * 单点登录应用秘钥
     */
    private String clientSecret;

    /**
     * 单点登录服务地址
     */
    private String url;

    /**
     * 单点登录回调地址
     */
    private String callbackUrl;
}
