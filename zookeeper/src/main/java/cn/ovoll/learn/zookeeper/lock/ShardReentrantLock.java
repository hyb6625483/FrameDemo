package cn.ovoll.learn.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式可重入锁
 */
@Component
public class ShardReentrantLock {

    @Resource
    private CuratorFramework client;

    /**
     * 加锁处理，会一直尝试获取锁，直到加锁成功
     * @param lockKey 锁名称
     * @param supplier 处理逻辑
     * @param <T> 返回类型
     * @return 返回结果
     * @throws Exception 获取锁出现异常
     */
    public <T> T lock(String lockKey, Supplier<T> supplier) throws Exception {
        // 创建锁
        InterProcessLock lock = new InterProcessMutex(client, lockKey);
        try {
            // 获取锁
            lock.acquire();
            return supplier.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取锁出现异常");
        } finally {
            try {
                // 释放锁
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加锁处理，在指定时间内尝试获取锁，加锁失败返回 null
     * @param lockKey 锁名称
     * @param supplier 处理逻辑
     * @param timeOut 等待时间
     * @param unit 时间单位
     * @param <T> 返回类型
     * @return 返回结果
     * @throws Exception 获取锁出现异常
     */
    public <T> T tryLock(String lockKey, Supplier<T> supplier, int timeOut, TimeUnit unit) throws Exception {
        // 创建锁
        InterProcessLock lock = new InterProcessMutex(client, lockKey);
        try {
            // 获取锁
            if (lock.acquire(timeOut, unit)) {
                return supplier.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取锁出现异常");
        } finally {
            try {
                // 释放锁
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
