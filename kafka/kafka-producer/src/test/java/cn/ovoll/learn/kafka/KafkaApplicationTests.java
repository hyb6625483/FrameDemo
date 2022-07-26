package cn.ovoll.learn.kafka;

import cn.ovoll.learn.kafka.constants.TopicConstant;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class KafkaApplicationTests {

    @Resource
    private KafkaTemplate<String, Object> template;
    @Resource
    private KafkaProperties properties;

    @Test
    void contextLoads() {
        template.send(TopicConstant.TEST_TOPIC, "测试 Kafka 发送消息").addCallback(success -> {
            System.out.println("消息发送成功");
        }, failure -> {
            System.out.println("消息发送失败");
        });
    }

    @Test
    void listTopic() throws ExecutionException, InterruptedException {
        AdminClient client = KafkaAdminClient.create(properties.buildAdminProperties());
        ListTopicsResult result = client.listTopics();
        System.out.println(result.names().get());
    }

}
