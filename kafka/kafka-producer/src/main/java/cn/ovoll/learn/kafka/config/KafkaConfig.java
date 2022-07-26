package cn.ovoll.learn.kafka.config;

import cn.ovoll.learn.kafka.constants.TopicConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    /**
     * 创建一个新的 Topic，分区数为 3，副本数为 2
     * @return topic
     */
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(TopicConstant.TEST_TOPIC, 3, (short) 2);
    }
}
