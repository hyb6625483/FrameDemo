package cn.ovoll.learn.websocket.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/web/{userId}") //WebSocket客户端建立连接的地址
@Component
public class WebSocketServer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    // 存活的session集合（使用线程安全的map保存）
    private static Map<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    // 记录当前在线连接数
    private static int onlineCount = 0;
    private Session session;

    /**
     * 建立连接的回调方法
     *
     * @param session 与客户端的WebSocket连接会话
     * @param userId  用户名，WebSocket支持路径参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        System.err.println("session : " + session.getId() + ",this.SessionId : " + this.session.getId());
        webSocketSet.put(userId, this);
        addOnlineCount();
        logger.info(userId + "进入连接，当前连接数：" + getOnlineCount() + ",存活Session数：" + webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> map = JSON.parseObject(message);
        map.forEach((key, value) -> {
            System.out.println("key is " + key + ",value is " + value);
        });
        String userId = map.get("userId").toString();
        if (userId != null && !userId.equals("")) // 给指定用户发送消息
            sendMessageToAll(map.get("msg").toString(), map.get("userId").toString());
        else  // 群发消息
            sendMessageToAll(map.get("msg").toString());
    }

    @OnError
    public void onError(Throwable error) {
        logger.info("发生错误");
        error.printStackTrace();
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketSet.remove(userId);
        subOnlineCount();
        logger.info(userId + " 关闭连接，当前连接数：" + getOnlineCount() + ",存活Session数：" + webSocketSet.size());
    }

    /**
     * 单独发送消息
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息到指定客户端
     */
    public void sendMessageToAll(String message, String id) {
        webSocketSet.forEach((userId, webSocketServer) -> {
            try {
                if (userId.equals(id)) {
                    webSocketServer.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 群体发送消息
     */
    public void sendMessageToAll(String message) {
        webSocketSet.forEach((userId, webSocketServer) -> {
            try {
                webSocketServer.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        onlineCount--;
    }
}
