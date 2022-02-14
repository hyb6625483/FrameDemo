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
        template.convertAndSend(RabbitConstants.EXCHANGE_NAME,"", "测试Springboot整合RabbitMQ");
    }
}
