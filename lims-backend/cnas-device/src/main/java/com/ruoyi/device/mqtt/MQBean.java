package com.ruoyi.device.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MQBean {

	@Bean("mqClient") // 启动WEB服务器的时候调用此方法初始化
	public MQClient myMQTTClient(){
		MQClient mqClient = new MQClient();
		return mqClient;
	}
}
