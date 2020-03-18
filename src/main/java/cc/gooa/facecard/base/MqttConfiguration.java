package cc.gooa.facecard.base;


import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
    /**
     * 链接地址
     */
    private String host;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 主题
     */
    private String topic;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 保持时间
     */
    private int keepalive;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientid) {
        this.clientId = clientid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }
}
