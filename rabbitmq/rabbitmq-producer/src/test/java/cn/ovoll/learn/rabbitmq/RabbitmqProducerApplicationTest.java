package cn.ovoll.learn.rabbitmq;

import cn.ovoll.learn.rabbitmq.constant.RabbitConstants;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RabbitmqProducerApplicationTest {

    @Resource
    private RabbitTemplate template;

    @Test
    void test() {
        template.convertAndSend(RabbitConstants.FANOUT_EXCHANGE,"", "测试Springboot整合RabbitMQ");
    }

    @Test
    void testConfirmCallback() {
        // correlationData：投递消息的相关信息
        // ack：是否成功投递消息至交换机
        // cause：失败原因
        template.setConfirmCallback((correlationData, ack, cause)->{
            System.out.println("confirm回调方法执行了。。。");
            if (ack) {
                System.out.println("消息发送成功");
            } else {
                System.out.println("消息发送失败，异常原因：" + cause);
                // TODO 消息重发
            }
        });
        template.convertAndSend(RabbitConstants.FANOUT_EXCHANGE + 1,"", "测试Springboot整合RabbitMQ");
    }

    @Test
    void testReturnCallback() {
        // 1.设置交换机处理消息投递失败的模式
        template.setMandatory(true);
        // 2.设置消息回退回调
        // message: 消息对象
        // replyCode: 错误码
        // replyText: 错误消息
        // exchange: 交换机
        // routingKey: 路由key
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey)->{
            System.out.println("执行了return回调");
            // TODO 消息重试
        });
        template.convertAndSend(RabbitConstants.DIRECT_EXCHANGE,"direct", "测试Springboot整合RabbitMQ");
    }

    @Test
    void testTTL() {
        template.convertAndSend(RabbitConstants.DIRECT_EXCHANGE, "direct", "测试Springboot整合RabbitMQ", message -> {
            // 设置消息过期时间
            message.getMessageProperties().setExpiration("5000");
            // 返回该消息
            return message;
        });
    }

    @Test
    void testDeadLetter() {
        for (int i = 0; i < 11; i++) {
            template.convertAndSend(RabbitConstants.TOPIC_EXCHANGE,"topic.test", i + ".测试Springboot整合RabbitMQ");
        }
    }
}
