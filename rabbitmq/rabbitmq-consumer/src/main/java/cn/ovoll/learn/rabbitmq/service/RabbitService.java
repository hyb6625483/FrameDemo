package cn.ovoll.learn.rabbitmq.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    @RabbitListener(queues = "myTestAmq.queue")
    @RabbitHandler
    public void receiver(@Payload String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        // 消费者操作
        System.out.println("收到消息："+ msg);

        // 手动签收消息
        channel.basicAck(deliveryTag, true);
    }
}
