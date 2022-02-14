package cn.ovoll.learn.rabbitmq.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RoutingProducerDemo {

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
        // 5.创建交换机
        String exchangeName = "test_direct";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        // 6.创建队列
        String queueName1 = "test_direct_queue1";
        String queueName2 = "test_direct_queue2";
        channel.queueDeclare(queueName1,true,false, false, null);
        channel.queueDeclare(queueName2,true,false, false, null);
        // 7.绑定交换机
        // queue：队列名称
        // exchange：交换机名称
        // routing key：路由键，绑定规则
        //   如果交换机类型为fanout，routing key设置为空字符串
        channel.queueBind(queueName1, exchangeName, "error");
        channel.queueBind(queueName2, exchangeName, "info");
        channel.queueBind(queueName2, exchangeName, "warn");
        channel.queueBind(queueName2, exchangeName, "error");
        // 8.发送消息
        byte[] body = StandardCharsets.UTF_8.encode("hello").array();
        channel.basicPublish(exchangeName, "info", null, body);
        channel.basicPublish(exchangeName, "error", null, body);
        // 9.关闭连接
        channel.close();
        connection.close();
    }
}
