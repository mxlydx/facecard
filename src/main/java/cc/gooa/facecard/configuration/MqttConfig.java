package cc.gooa.facecard.configuration;

import cc.gooa.facecard.base.MqttConfiguration;
import cc.gooa.facecard.service.impl.PushCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MqttConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static volatile MqttConfig mqttPushClient = null;
    @Autowired
    Environment env;

    @Bean
    public MqttClient mqttClient() {
        logger.info("start connect mqtt");
        int reconnect = env.getProperty("facecard.reconnect", Integer.class);
        MqttConnectOptions options = this.mqttConnectOptions();
        MqttConfiguration mqttConfiguration = this.loadMqttConfiguration();
        try {
            MqttClient  client = new MqttClient(mqttConfiguration.getHost(), mqttConfiguration.getClientId(), new MemoryPersistence());
            options.setKeepAliveInterval(20);
            client.setCallback(new PushCallback());
            client.connect(options);
            client.subscribe("mybroker/clients/#");
            return  client;
        } catch (MqttException e) {
            try {
                logger.info("connect to "+ mqttConfiguration.getHost() +" mqtt failed," + reconnect + "s later try to reconnect");
                Thread.sleep(reconnect * 1000);
                return this.mqttClient();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
    @Bean
    public static MqttConfig getInstance() {

        if (null == mqttPushClient) {
            synchronized (MqttConfig.class) {
                if (null == mqttPushClient) {
                    mqttPushClient = new MqttConfig();
                }
            }
        }
        return mqttPushClient;
    }
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        MqttConfiguration mqttConfiguration = this.loadMqttConfiguration();
        // 设置连接的用户名
        mqttConnectOptions.setUserName(mqttConfiguration.getUsername());
        // 设置连接的密码
        mqttConnectOptions.setPassword(mqttConfiguration.getPassword().toCharArray());
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(mqttConfiguration.getKeepalive());
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(mqttConfiguration.getTimeout());
        mqttConnectOptions.setServerURIs(new String[] {mqttConfiguration.getHost()});
        return mqttConnectOptions;
    }
    @Bean
    @ConfigurationProperties("mqtt")
    public MqttConfiguration loadMqttConfiguration() {
        return new MqttConfiguration();
    }
}
