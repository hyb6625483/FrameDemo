package cn.ovoll.learn.rabbitmq.config;

import cn.ovoll.learn.rabbitmq.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 创建一个发布订阅模式的交换机
     * @return
     */
    @Bean
    public Exchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(RabbitConstants.EXCHANGE_NAME).build();
    }

    @Bean
    public Queue fanoutQueue1() {
        return QueueBuilder.durable(RabbitConstants.QUEUE_NAME).build();
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange()).with("").noargs();
    }

    /**
     * rabbitMQ序列化格式
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
