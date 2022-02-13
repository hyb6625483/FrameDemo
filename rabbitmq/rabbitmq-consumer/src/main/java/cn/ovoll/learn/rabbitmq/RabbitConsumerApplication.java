package cn.ovoll.learn.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit   //开启基于注解的RabbitMQ模式
@SpringBootApplication
public class RabbitConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitConsumerApplication.class);
    }
}
