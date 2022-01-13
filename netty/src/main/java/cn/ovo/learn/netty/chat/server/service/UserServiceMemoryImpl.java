package cn.ovo.learn.netty.chat.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceMemoryImpl implements UserService {

    private final Map<String, String> ALL_USER = new ConcurrentHashMap<>();

    {
        ALL_USER.put("zhangsan","123");
        ALL_USER.put("lisi","123");
        ALL_USER.put("wangwu","123");
        ALL_USER.put("yuting","123");
        ALL_USER.put("pangzi","123");
    }

    @Override
    public boolean login(String name, String password) {
        String pass = ALL_USER.get(name);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
