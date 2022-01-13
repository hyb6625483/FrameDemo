package cn.ovo.learn.netty.chat.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private final Map<String, Channel> USER_MAP = new ConcurrentHashMap<>();
    private final Map<Channel, String> CHANNEL_MAP = new ConcurrentHashMap<>();
    private final Map<Channel, Map<String, Object>> ATTRIBUTE_MAP = new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel, String username) {
        USER_MAP.put(username, channel);
        CHANNEL_MAP.put(channel, username);
        ATTRIBUTE_MAP.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        String username = CHANNEL_MAP.remove(channel);
        ATTRIBUTE_MAP.remove(channel);
        USER_MAP.remove(username);
    }

    @Override
    public Object getAttribute(Channel channel, String name) {
        return Optional.ofNullable(ATTRIBUTE_MAP.get(channel)).map(attr -> attr.get(name)).get();
    }

    @Override
    public void setAttribute(Channel channel, String name, Object value) {
        Map<String, Object> attr = ATTRIBUTE_MAP.get(channel);
        attr.putIfAbsent(name, value);
    }

    @Override
    public Channel getChannel(String username) {
        return USER_MAP.get(username);
    }
}
