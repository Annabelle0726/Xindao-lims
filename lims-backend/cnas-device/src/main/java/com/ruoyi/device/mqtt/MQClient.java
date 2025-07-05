package com.ruoyi.device.mqtt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQClient {

	private static MqttClient client;
	public static MqttClient getClient() {
		return client;
	}
	public static void setClient(MqttClient client) {
		MQClient.client = client;
	}

	/**
	 * WEB服务器连接MQTT服务器的配置
	 * @param userName 账号
	 * @param password 密码
	 * @param outTime 超时时间
	 * @param KeepAlive 心跳检测时间
	 * @return
	 */
	private MqttConnectOptions getOption(String userName, String password, int outTime, int KeepAlive) {
		MqttConnectOptions option = new MqttConnectOptions();
		// 设置是否清空session,false表示服务器会保留客户端的连接记录，true表示每次连接到服务器都以新的身份连接
		option.setCleanSession(true);
		// 设置连接的用户名
		option.setUserName(userName);
		// 设置连接的密码
		option.setPassword(password.toCharArray());
		// 设置超时时间 单位为秒
		option.setConnectionTimeout(outTime);
		// 设置会话心跳时间 单位为秒 服务器会每隔(1.5*keepTime)秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
		option.setKeepAliveInterval(KeepAlive);
		// setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
		// option.setWill(topic, "close".getBytes(), 2, true);
		//设置最大速度
		option.setMaxInflight(1000);
		log.info("================>>>MQTT连接认证成功<<======================");
		return option;
	}

	/**
	 * WEB服务器连接MQTT服务器函数
	 * @param mqttConfig yml中MQTT的配置
	 */
	public void connect(MQConfig mqttConfig) throws MqttException {
		client = new MqttClient(mqttConfig.getUrl(), mqttConfig.getClientId(), new MemoryPersistence());
		MqttConnectOptions options = getOption(mqttConfig.getUsername(), mqttConfig.getPassword(),
				mqttConfig.getTimeout(), mqttConfig.getKeepAlive());
		MQClient.setClient(client);
		//连接失败调用回调函数，重新连接
		client.setCallback(new MQCallback<Object>(this, mqttConfig));
		if (!client.isConnected()) {
			client.connect(options);
			// 订阅主题
			MQSubscribe.subscribe_0(mqttConfig.getSubscribe());
			log.info("================>>>MQTT连接成功<<======================");
		} else {// 这里的逻辑是如果连接不成功就重新连接
			client.disconnect();
			client.connect(options);
		}
	}

	/**
	 * WEB服务器与MQTT服务器的断线重连
	 * @throws Exception
	 */
	public Boolean reConnect() throws Exception {
		Boolean isConnected = false;
		if (null != client) {
			client.connect();
			if (client.isConnected()) {
				isConnected = true;
			}
		}
		return isConnected;
	}

	/**
	 * 异常关闭服务，WEB服务器与MQTT服务器的断开连接函数
	 */
	@SneakyThrows
	public void close(){
		client.close();
		client.disconnect();
		log.info("================>>异常关闭与mqtt服务器的连接<<======================");
	}
}
