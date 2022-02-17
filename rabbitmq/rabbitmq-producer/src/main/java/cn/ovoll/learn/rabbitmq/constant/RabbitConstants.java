package cn.ovoll.learn.rabbitmq.constant;

public interface RabbitConstants {

    String FANOUT_EXCHANGE = "springboot_fanout";

    String DIRECT_EXCHANGE = "springboot_direct";

    String TOPIC_EXCHANGE = "springboot_topic";

    String DEAD_EXCHANGE = "springboot_dead";

    String ORDER_EXCHANGE = "springboot_order";

    String DLX_ORDER_EXCHANGE = "springboot_order_dlx";

    String FANOUT_QUEUE = "springboot_fanout_queue";

    String DIRECT_QUEUE = "springboot_direct_queue";

    String TOPIC_QUEUE = "springboot_topic_queue";

    String DEAD_QUEUE = "springboot_dead_queue";

    String ORDER_QUEUE = "springboot_order_queue";

    String DLX_ORDER_QUEUE = "springboot_order_dlx_queue";
}
