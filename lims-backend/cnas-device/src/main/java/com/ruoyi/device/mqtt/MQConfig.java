package com.ruoyi.device.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class MQConfig {

	/**
	 * MQTT-服务端-IP
	 */
	@Value("${mqtt.url}")
	private String url;

	/**
	 * MQTT-服务端-用户名
	 */
	@Value("${mqtt.username}")
	private String username;

	/**
	 * MQTT-服务端-密码
	 */
	@Value("${mqtt.password}")
	private String password;

	/**
	 * 超时时间
	 */
	@Value("${mqtt.timeout}")
	private int timeout;

	/**
	 * 心跳检测时间
	 */
	@Value("${mqtt.keepalive}")
	private int keepAlive;

	/**
	 * 心跳包级别
	 */
	@Value("${mqtt.qos}")
	private int qos;

	/**
	 * 服务端连接超时时间
	 */
	@Value("${mqtt.completion-timeout}")
	private int completionTimeout;

	/**
	 * clientId
	 */
	@Value("${mqtt.clientId}")
	private String clientId;

	/**
	 * 订阅主题
	 */
	@Value("${mqtt.subscribe}")
	private String subscribe;
}
