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

    /**
     * 创建一个路由模式的交换机
     * @return
     */
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
     * 创建一个死信队列的交换机
     * @return
     */
    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.topicExchange(RabbitConstants.DEAD_EXCHANGE).build();
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(RabbitConstants.DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead.#").noargs();
    }

    /**
     * 创建一个通配符模式的交换机
     * @return
     */
    @Bean
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(RabbitConstants.TOPIC_EXCHANGE).build();
    }

    @Bean
    public Queue topicQueue() {
        return QueueBuilder.durable(RabbitConstants.TOPIC_QUEUE).ttl(10000).maxLength(10)
                .deadLetterExchange(RabbitConstants.DEAD_EXCHANGE).deadLetterRoutingKey("dead.test").build();
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("topic.#").noargs();
    }

    /**
     * 创建一个延迟队列
     */
    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.topicExchange(RabbitConstants.ORDER_EXCHANGE).build();
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(RabbitConstants.ORDER_QUEUE).ttl(10000).maxLength(10)
                .deadLetterExchange(RabbitConstants.DLX_ORDER_EXCHANGE).deadLetterRoutingKey("order.check").build();
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.#").noargs();
    }

    @Bean
    public Exchange dlxOrderExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DLX_ORDER_EXCHANGE).build();
    }

    @Bean
    public Queue dlxOrderQueue() {
        return QueueBuilder.durable(RabbitConstants.DLX_ORDER_QUEUE).build();
    }

    @Bean
    public Binding dlxOrderBinding() {
        return BindingBuilder.bind(dlxOrderQueue()).to(dlxOrderExchange()).with("order.check").noargs();
    }



    /**
     * rabbitMQ序列化格式
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
