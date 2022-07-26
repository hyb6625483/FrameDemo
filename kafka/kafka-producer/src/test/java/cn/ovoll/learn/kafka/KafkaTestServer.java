package cn.ovoll.learn.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.IOException;

@SpringBootTest
@EmbeddedKafka(count = 3, zookeeperPort = 2181, ports = {9092, 9093, 9094}, partitions = 3)
public class KafkaTestServer {

    @Test
    void contextLoads() throws IOException {
        int read = System.in.read();
    }
}
