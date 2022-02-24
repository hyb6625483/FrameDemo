package cn.ovoll.learn.canal.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;

@Configuration
public class CanalConfig {

    public CanalConnector canalConnector(CanalProperties prop) {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(prop.getHost(), prop.getPort()),
                prop.getDestination(),
                prop.getUsername(),
                prop.getPassword());
        try {
            connector.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(prop.getSubscribe())) {
            connector.subscribe();
        } else {
            connector.subscribe(prop.getSubscribe());
        }
        connector.rollback();
        return connector;
    }

}
