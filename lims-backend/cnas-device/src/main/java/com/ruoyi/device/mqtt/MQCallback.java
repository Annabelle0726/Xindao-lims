package com.ruoyi.device.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.device.service.CollectBridgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class MQCallback<component> implements MqttCallback {

    private MQClient mqClient; // MQTT连接数据

    private MQConfig mqConfig; // yml配置数据

    private static MQCallback mqCallback;

    @Resource
    private CollectBridgeService collectBridgeService;

    @PostConstruct
    public void init() {
		mqCallback = this;
         // 初使化时将已静态化的configParam实例化
		mqCallback.collectBridgeService = this.collectBridgeService;
    }

    public MQCallback(MQClient mqClient, MQConfig mqConfig) {
        this.mqClient = mqClient;
        this.mqConfig = mqConfig;
    }

    /** 连接丢失后，一般在这里面进行重连 **/
    @SneakyThrows
    @Override
    public void connectionLost(Throwable cause) {
        /** 连接丢失后，一般在这里面进行重连 **/
        if (mqClient != null) {
            while (true) {
                try {
                    log.info("==============》》》[MQTT] 连接丢失，尝试重连...");
                    MQClient mqttPushClient = new MQClient();
                    mqttPushClient.connect(mqConfig);
                    if (mqClient.getClient().isConnected()) {
                        log.info("=============>>重连成功");
                    }
                    break;
                } catch (Exception e) {
                    log.error("=============>>>[MQTT] 连接断开，重连失败！<<=============");
                    continue;
                }
            }
        }
        log.info(cause.getMessage());
    }

    /**
     * MQTT服务器向WEB服务器发送的数据会执行到这里面，官方话称为：订阅后的消息
     * @param topic 主题：也称为底层网关唯一标识
     * @param message 信息
     * @throws Exception 报错
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            String parse = new String(message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(parse);
            // 填充采集数据
            mqCallback.collectBridgeService.addBridgeValue(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("============》》接收消息主题异常 : " + e.getMessage());
        }
    }

    /**
     * WEB服务器向MQTT服务器发送的数据会执行到这里面
     * 官方话称为：发布后会执行到这里
     * @param token 连接token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
//        log.info("==========发布信息={}==========", token.isComplete());
    }
}
