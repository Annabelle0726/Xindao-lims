package com.ruoyi.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {
    private String endpoint;
    private int port;
    private String accessKey;
    private String secretKey;
    private Boolean secure;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(endpoint, port, secure)
                .credentials(accessKey, secretKey)
                .build();
    }
}
