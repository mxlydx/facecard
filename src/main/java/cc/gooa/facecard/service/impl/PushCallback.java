package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.base.RedisKey;
import cc.gooa.facecard.configuration.MqttConfig;
import cc.gooa.facecard.model.BusCreditLog;
import cc.gooa.facecard.service.BusCreditLogService;
import cc.gooa.facecard.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Base64;
import java.util.Date;

public class PushCallback implements MqttCallback {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Environment env;
    @Autowired
    BusCreditLogService busCreditLogService;

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
        if (topic != null && topic.startsWith("mqtt/face")) {
            JSONObject data = JSONObject.parseObject(mqtMessage);
            JSONObject info = JSONObject.parseObject(data.getString("info"));
            String operator = data.getString("operator");
            if (topic.endsWith("Ack")) {
                switch (operator) {
                    case "EditPerson-Ack":
                        if (data != null && "ok".equals(info.getString("result"))) {
                            String[] topics = topic.split("/");
                            String deviceId = topics[2];
                            String customId = info.getString("customId");
                            RedisUtil.setValue(RedisKey.SYNOED_IDS.getKey() + deviceId + ":" + customId, "1");
                        }
                        break;
                    case "DelPerson":
                        if (data != null && "ok".equals(info.getString("result"))) {
                            String[] topics = topic.split("/");
                            String deviceId = topics[2];
                            String customId = info.getString("customId");
                            RedisUtil.setValue(RedisKey.SYNOED_IDS.getKey() + deviceId + ":" + customId, null);
                        }
                        break;
                     default:
                         System.out.println("nothing");
                         break;
                }

            } else if (topic.endsWith("Rec")) {
                if (data != null) {
                    // 组织入库bean
                    BusCreditLog bean = new BusCreditLog();
                    bean.setCustomercode(info.getString("customId"));
                    bean.setCard(info.getString("cardCode"));
                    bean.setDevicecode(info.getString("facesluiceId"));
                    String base64 = info.getString("pic");
                    if (base64 != null) {
                        bean.setPict(Base64.getDecoder().decode(base64));
                    }
                    Date creditTime = new Date();
                    try {
                        creditTime = info.getDate("time");
                    } catch (Exception e) {
                        logger.error("get ValidBegin to date error ");
                    }
                    bean.setCredittime(creditTime);
                    bean.setUploadtime(new Date());
                    bean.setStatus(1);
                    try {
                        busCreditLogService.insert(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    } finally {

                    }
                }
            } else if (topic.endsWith("disconnected")) {
                String clientId = String.valueOf(data.get("clientid"));
                logger.info("客户端已掉线：{}", clientId);
            } else if (topic.endsWith("connected")) {
                String clientId = String.valueOf(data.get("clientid"));
                logger.info("客户端已上线：{}", clientId);
            }


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("消息发送成功");
    }
}
