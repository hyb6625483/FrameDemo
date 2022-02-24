package cn.ovoll.learn.canal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.canal")
public class CanalProperties {

    /**
     * IP地址
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 服务器实例
     */
    private String destination;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * zookeeper地址
     */
    private String zookeeper;

    /**
     * 订阅规则
     * 1.所有表：.*   or  .*\\..*
     * 2.canal schema下所有表： canal\\..*
     * 3.canal下的以canal打头的表：canal\\.canal.*
     * 4.canal schema下的一张表：canal\\.test1
     * 5.多个规则组合使用：canal\\..*,mysql.test1,mysql.test2 (逗号分隔)
     */
    private String subscribe;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }
}
