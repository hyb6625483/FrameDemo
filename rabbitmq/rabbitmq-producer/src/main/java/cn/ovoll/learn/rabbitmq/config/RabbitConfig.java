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
        return ExchangeBuilder.fanoutExchange(RabbitConstants.FANOUT_EXCHANGE).build();
    }

    @Bean
    public Queue fanoutQueue() {
        return QueueBuilder.durable(RabbitConstants.FANOUT_QUEUE).build();
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange()).with("").noargs();
    }

    @Bean
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DIRECT_EXCHANGE).build();
    }

    @Bean
    public Queue directQueue() {
        return QueueBuilder.durable(RabbitConstants.DIRECT_QUEUE).build();
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("direct").noargs();
    }

    /**
     * rabbitMQ序列化格式
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
