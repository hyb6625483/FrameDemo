package cn.ovoll.learn.websocket.controller;

import cn.ovoll.learn.websocket.service.WebSocketServer;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class WebSocketController {
    @Resource
    private WebSocketServer webSocketServer;

    @PostMapping("/send")
    public void send(String msg) {
        Map<String, Object> map = JSON.parseObject(msg);
        map.forEach((key, value) -> {
            System.out.println("key is "+ key + ",value is " + value);
        });
        webSocketServer.sendMessageToAll(map.get("msg").toString());
    }
}
