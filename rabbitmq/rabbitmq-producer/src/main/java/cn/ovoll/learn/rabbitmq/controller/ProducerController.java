package cn.ovoll.learn.rabbitmq.controller;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class ProducerController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private AmqpAdmin amqpAdmin;

    public String test1(String param) {
        amqpAdmin.declareExchange(new DirectExchange("myTestAmq.exchange"));
        amqpAdmin.declareQueue(new Queue("myTestAmq.queue"));
        amqpAdmin.declareBinding(new Binding("myTestAmq.queue", Binding.DestinationType.QUEUE, "myTestAmq.exchange", "", null));
        rabbitTemplate.convertAndSend("myTestAmq.exchange","",param);
        return "index";
    }
}
