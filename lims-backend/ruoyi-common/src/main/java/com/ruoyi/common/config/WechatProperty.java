package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZTT
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatProperty {

	/**
	 * 装备电缆：报检通知url
	 */
	private String examiningUrl;


}
