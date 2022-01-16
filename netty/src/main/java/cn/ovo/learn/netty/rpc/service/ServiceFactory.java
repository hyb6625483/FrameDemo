package cn.ovo.learn.netty.rpc.service;

import cn.ovo.learn.netty.chat.config.Config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {

    private static final Properties PROP;
    private static final Map<Class<?>, Object> CLASS_CACHE = new ConcurrentHashMap<>();

    static {
        try (InputStream inputStream = Config.class.getResourceAsStream("/application.properties")) {
            PROP = new Properties();
            PROP.load(inputStream);
            Set<String> names = PROP.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")) {
                    Class<?> interfaceClazz = Class.forName(name);
                    Class<?> instanceClass = Class.forName(PROP.getProperty(name));
                    CLASS_CACHE.put(interfaceClazz, instanceClass.newInstance());
                }
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> interfaceClazz) {
        return (T) CLASS_CACHE.get(interfaceClazz);
    }
}
