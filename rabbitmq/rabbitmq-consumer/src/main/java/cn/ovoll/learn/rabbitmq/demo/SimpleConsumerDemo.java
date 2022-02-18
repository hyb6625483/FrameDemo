package cn.ovoll.learn.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleConsumerDemo {

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
        // 5.创建队列
        channel.queueDeclare("hello_world", true, false, false, null);
        // 6.消费消息
        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 回调方法，当收到消息后，会自动执行该方法
             * @param consumerTag 标识
             * @param envelope 环境信息，如交换机、routing key。。。
             * @param properties 配置信息
             * @param body 消息主体
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Consumer Tag: " + consumerTag);
                System.out.println("Exchange: " + envelope.getExchange());
                System.out.println("Routing Key: " + envelope.getRoutingKey());
                System.out.println("properties: " + properties);
                System.out.println("body: " + new String(body));
            }
        };
        channel.basicConsume("hello_world", true, consumer);
    }
}
