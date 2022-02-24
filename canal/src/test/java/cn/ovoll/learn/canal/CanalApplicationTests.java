package cn.ovoll.learn.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CanalApplicationTests {

    @Test
    void contextLoads() {
        // 创建连接客户端
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("47.119.141.144", 8081),
                "example", "", "");
        int batchSize = 1000;
        // 建立连接
        connector.connect();
        // 指定订阅哪个库的哪个表，订阅规则
        // 1.所有表：.*   or  .*\\..*
        // 2.canal schema下所有表： canal\\..*
        // 3.canal下的以canal打头的表：canal\\.canal.*
        // 4.canal schema下的一张表：canal\\.test1
        // 5.多个规则组合使用：canal\\..*,mysql.test1,mysql.test2 (逗号分隔)
        connector.subscribe("vptv_db\\..*");
        // 回滚到未进行确认消费的地方
        connector.rollback();
        while (true) {
            // 获取指定数量的数据
            Message message = connector.getWithoutAck(batchSize);
            long id = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            // 判断是否有数据
            if (id == -1 || entries.isEmpty()) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // TODO 处理数据
            for (CanalEntry.Entry entry : entries) {
                // 如果是事务日志，则跳过
                if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                    continue;
                }
                try {
                    CanalEntry.RowChange change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    CanalEntry.EventType eventType = change.getEventType();
                    // 如果是查询日志或者DDL语句，跳过
                    if (eventType == CanalEntry.EventType.QUERY || change.getIsDdl()) {
                        System.out.println("sql => " + change.getSql());
                        continue;
                    }
                    for (CanalEntry.RowData rowData : change.getRowDatasList()) {
                        if (eventType == CanalEntry.EventType.DELETE) {
                            // 删除语句，打印删除前的数据
                            System.out.println("-------> delete <------");
                            printColumn(rowData.getBeforeColumnsList());
                        } else if (eventType == CanalEntry.EventType.INSERT) {
                            // 插入语句，打印插入后的数据
                            System.out.println("-------> insert <------");
                            printColumn(rowData.getAfterColumnsList());
                        } else {
                            System.out.println("-------> update <------");
                            System.out.println("-------> before");
                            printColumn(rowData.getBeforeColumnsList());
                            System.out.println("-------> after");
                            printColumn(rowData.getAfterColumnsList());
                        }
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException("解析binlog数据出现异常 , data:" + entry.toString(), e);
                }
            }
            // 确认数据被消费
            connector.ack(id);
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}
