package com.ruoyi.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class MQSubscribe {

	/**
	 * 订阅某个主题：MQTT服务器向WEB服务器发送数据
	 *主题：也称为底层网关唯一标识
	 * @param topic 设备编号，与底层交互的唯一标识
	 * @param qos MQTT服务器向WEB服务器发送数据
	 *            qos为0：只向WEB服务器发送一次；
	 *            qos为1：至少向WEB服务器发送一次，接收方会响应一个报文；
	 *            qos为2：两者会进行至少两次请求/响应流程，避免数据在传输中的丢失
	 */
	private static void subscribe(String topic, int qos) {
		try {
			MQClient.getClient().subscribe(topic,qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 订阅某个主题，通信质量：qos默认为0
	 * 主题：也称为底层网关唯一标识
	 * @param topic 设备编号，与底层交互的唯一标识
	 */
	public static void subscribe_0(String topic) {
		subscribe(topic, 0);
	}

	/**
	 * 订阅某个主题，通信质量：qos默认为1
	 * 主题：也称为底层网关唯一标识
	 * @param topic 设备编号，与底层交互的唯一标识
	 */
	public void subscribe_1(String topic) {
		subscribe(topic, 1);
	}

	/**
	 * 订阅某个主题，通信质量：qos默认为2
	 * 主题：也称为底层网关唯一标识
	 * @param topic 设备编号，与底层交互的唯一标识
	 */
	public void subscribe_2(String topic) {
		subscribe(topic, 2);
	}

	public void OffSubscribe(String topic) {
		try {
			MQClient.getClient().unsubscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
