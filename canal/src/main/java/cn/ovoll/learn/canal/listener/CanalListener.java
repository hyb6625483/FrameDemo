package cn.ovoll.learn.canal.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CanalListener {

    private static final String CANAL_QUEUE = "canal.queue";

    @RabbitListener(queues = CANAL_QUEUE)
    @RabbitHandler
    public void listener(@Payload String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        try {
            // 消费者操作
            System.out.println("收到消息："+ msg);
            // 手动签收消息
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            // 第三个参数表示是否重回队列
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ioException) {
                System.out.println("拒绝签收失败，请处理消息：" + msg);
            }
        }
    }
}
