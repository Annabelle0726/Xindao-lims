package com.ruoyi.device.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQPublic {

	/**
	 * 通信质量qos为0：表示WEB服务器向MQTT服务器只发送一次，不管MQTT服务器有没有收到
	 * WEB服务器向MQTT服务器发布数据，此方法封装了publish函数
	 * @param topic 向底层网关发送数据，官方话：称此为主题，向那个主题发送数据 网关：即主题，设备唯一标识
	 * @param pushMessage WEB服务器向MQTT服务器发送的数据
	 */
	public void publish_0(int qos, String topic, byte[] pushMessage) {
		publish(0, false, topic, pushMessage);
	}

	/**
	 * 通信质量qos为1：表示WEB服务器向MQTT服务器发送数据，MQTT服务器一定会收到一次数据，如果MQTT服务器没有响应“收到数据”，那么WEB服务器就会一直发送数据
	 * WEB服务器向MQTT服务器发布数据，调用此函数，此方法封装了publish函数，通信质量qos为1
	 * @param topic 向底层网关发送数据，官方话：称此为主题，向那个主题发送数据 网关：即主题，设备唯一标识
	 * @param pushMessage WEB服务器向MQTT服务器发送的数据
	 */
	public void publish_1(String topic, byte[] pushMessage) {
		publish(1, false, topic, pushMessage);
	}

	/**
	 *通信质量qos为2：表示WEB服务器向MQTT服务器发送数据，两者会进行至少两次请求/响应流程，避免数据在传输中的丢失，但是相应的也会消耗计算机中的资源
	 * WEB服务器向MQTT服务器发布数据，此方法封装了publish函数，通信质量：2
	 * @param topic 向底层网关发送数据，官方话：称此为主题，向那个主题发送数据 网关：即主题，设备唯一标识
	 * @param pushMessage WEB服务器向MQTT服务器发送的数据
	 */
	public void publish_2(String topic, byte[] pushMessage) {
		publish(2,false, topic, pushMessage);
	}

	/**
	 * 发布函数：WEB服务器向MQTT服务器发送数据
	 *
	 * @param qos 通信质量
	 * @param retained 默认：false-非持久化（是指一条消息消费完，就会被删除；持久化，消费完，还会保存在服务器中，当新的订阅者出现，继续给新订阅者消费）
	 * @param topic 向底层网关发送数据，官方话：称此为主题，向那个主题发送数据 网关：即主题，设备唯一标识
	 * @param pushMessage WEB服务器向MQTT服务器发送的数据
	 */
	public void publish(int qos, boolean retained, String topic, byte[] pushMessage) {
		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		// 将String[]数组转换为byte数组发送
		message.setPayload(pushMessage);
		MqttTopic mTopic = MQClient.getClient().getTopic(topic);
		if (null == mTopic) {
			log.error("===============>>>MQTT {} 不存在<<=======================",topic);
		}
		MqttDeliveryToken token;
		try {
			token = mTopic.publish(message);
			token.waitForCompletion();
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
