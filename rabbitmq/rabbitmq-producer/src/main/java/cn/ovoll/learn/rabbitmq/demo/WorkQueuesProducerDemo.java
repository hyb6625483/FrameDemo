package cn.ovoll.learn.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkQueuesProducerDemo {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 2.设置参数
        factory.setHost("192.168.31.4");
        factory.setPort(5672);
        factory.setVirtualHost("/demo");
        factory.setUsername("admin");
        factory.setPassword("admin123.");
        // 3.建立连接Connection
        Connection connection = factory.newConnection();
        // 4.创建Channel
        Channel channel = connection.createChannel();
        // 5.创建Queue，如果没有，则创建队列
        // queue: 队列名称
        // durable：是否持久化
        // exclusive：
        //    1. 是否独占，只能有一个消费者监听这个队列
        //    2. 当Connection关闭时是否删除队列
        // autoDelete：是否自动删除，当没有Consumer时，自动删除
        // arguments：其它属性
        channel.queueDeclare("work_queues", true, false, false, null);
        // 6.发送消息
        for (int i = 1; i <= 10; i++) {
            byte[] body = StandardCharsets.UTF_8.encode(i + ": hello").array();
            channel.basicPublish("", "work_queues", null, body);
        }
        // 7. 关闭连接
        channel.close();
        connection.close();
    }
}
