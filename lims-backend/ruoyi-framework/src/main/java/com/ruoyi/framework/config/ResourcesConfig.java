package com.ruoyi.framework.config;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 通用配置
 *
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Value("${file.path}")
    private String filePath;

    @Value("${wordUrl}")
    private String wordUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");


        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        //设置文件虚拟路径映射
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + filePath + "/");
        registry.addResourceHandler("/word/**").addResourceLocations("file:" + wordUrl + "/");
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    // 全局格式化处理
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        Jackson2ObjectMapperBuilder json = Jackson2ObjectMapperBuilder.json();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        //localDateTime格式化
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat));
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat));
        module.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
        module.addSerializer(LocalDateTime.class, dateTimeSerializer);
        ObjectMapper objectMapper = json.modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();

        //date时间格式化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat.split(" ")[0]));

        // 设置格式化内容
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
    }
}
