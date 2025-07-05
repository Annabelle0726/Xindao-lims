package com.ruoyi.web;

import com.ruoyi.device.mqtt.MQClient;
import com.ruoyi.device.mqtt.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttApplicationRunner implements ApplicationRunner {

	@Autowired
	private MQConfig mqConfig;

	@Value("${mqtt.client}")
	private Boolean client;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (client) {
			MQClient mqttPushClient = new MQClient();
			mqttPushClient.connect(mqConfig);
		}
	}
}
