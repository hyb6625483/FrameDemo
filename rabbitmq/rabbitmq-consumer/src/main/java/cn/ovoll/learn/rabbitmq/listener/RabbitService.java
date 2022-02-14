package cn.ovoll.learn.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitService {

    private static final String QUEUE_NAME = "springboot_fanout_queue";

    @RabbitListener(queues = QUEUE_NAME)
    @RabbitHandler
    public void demo(Message msg) throws Exception {
        // 消费者操作
        System.out.println("收到消息："+ msg);
    }

//    @RabbitListener(queues = QUEUE_NAME)
//    @RabbitHandler
    public void receiver(@Payload String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        // 消费者操作
        System.out.println("收到消息："+ msg);

        // 手动签收消息
        channel.basicAck(deliveryTag, true);
    }
}
