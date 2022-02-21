package cn.ovoll.learn.zookeeper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {

    /**
     * Zookeeper服务连接IP与端口
     */
    private String address;

    /**
     * 重试次数
     */
    private int maxRetries;

    /**
     * 每次重试时间间隔，单位毫秒
     */
    private int baseSleepTime;

    /**
     * 会话超时时间，单位毫秒
     */
    private int sessionTimeout;

    /**
     * 连接创建超时时间，单位毫秒
     */
    private int connectionTimeout;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getBaseSleepTime() {
        return baseSleepTime;
    }

    public void setBaseSleepTime(int baseSleepTime) {
        this.baseSleepTime = baseSleepTime;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
