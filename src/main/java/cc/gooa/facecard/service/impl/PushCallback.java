package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.base.RedisKey;
import cc.gooa.facecard.configuration.MqttConfig;
import cc.gooa.facecard.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class PushCallback implements MqttCallback {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Environment env;

    @Override
    public void connectionLost(Throwable throwable) {
        int reconnect = env.getProperty("facecard.reconnect", Integer.class);
        try {
            MqttConfig.getInstance().mqttClient();
        } catch (Exception e) {
            try {
                Thread.sleep(reconnect * 1000);
                this.connectionLost(throwable);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String mqtMessage = new String(message.getPayload());
        logger.info("接收消息主题:" + topic);
        logger.info("接收消息Qos:" + message.getQos());
        logger.info("接收消息内容 :" + mqtMessage);
        if (topic !=null && topic.startsWith("mqtt/face") && topic.endsWith("Ack")) {
            JSONObject data = JSONObject.parseObject(mqtMessage);
            JSONObject info = JSONObject.parseObject(data.getString("info"));
            if (data!= null && "ok".equals(info.getString("result"))) {
                String[] topics = topic.split("/");
                String deviceId = topics[2];
                String customId = data.getString("customId");
                RedisUtil.setValue(RedisKey.SYNOED_IDS.getKey() + deviceId + ":" + customId, "1");
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("消息发送成功");
    }
}
