package cn.ovoll.learn.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework(ZookeeperProperties prop) {
        // 定义重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(prop.getBaseSleepTime(), prop.getMaxRetries());
        // 创建连接客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(prop.getAddress())
                .sessionTimeoutMs(prop.getSessionTimeout())
                .connectionTimeoutMs(prop.getConnectionTimeout())
                .build();
        // 开启连接
        client.start();
        return client;
    }
}
