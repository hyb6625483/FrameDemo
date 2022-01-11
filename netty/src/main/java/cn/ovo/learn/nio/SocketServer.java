package cn.ovo.learn.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程多路复用NIO实现
 *
 * @author huyangbo
 */
@Slf4j
public class SocketServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("Boss");
        // 创建Selector
        Selector boss = Selector.open();
        // 创建Channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 监听端口
        serverChannel.bind(new InetSocketAddress(8080));
        // 设置线程模式为非阻塞
        serverChannel.configureBlocking(false);
        // 绑定channel至selector，并监听连接事件
        serverChannel.register(boss, SelectionKey.OP_ACCEPT);
        // 创建工作线程
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger count = new AtomicInteger();
        // 处理事件
        while (true) {
            log.info("waiting event.... {}", serverChannel);
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    final SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    log.info("connected.... {}", channel);
                    workers[count.incrementAndGet() % workers.length].register(channel);
                }
            }
        }
    }

    /**
     * 工作线程类
     */
    static class Worker implements Runnable {

        private final Thread thread;
        private final Selector selector;
        private boolean init = false;

//        private final ConcurrentLinkedQueue<Runnable> QUEUE = new ConcurrentLinkedQueue<>();

        public Worker(String threadName) throws IOException {
            this.selector = Selector.open();
            this.thread = new Thread(this, threadName);
        }

        public void register(SocketChannel channel) throws ClosedChannelException {
            if (!init) {
                this.thread.start();
                this.init = true;
            }
//            QUEUE.add(() -> {
//                try {
//                    channel.register(selector, SelectionKey.OP_READ);
//                } catch (ClosedChannelException e) {
//                    e.printStackTrace();
//                }
//            });
            selector.wakeup();
            channel.register(selector, SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
//                    Optional.ofNullable(QUEUE.poll()).ifPresent(Runnable::run);
                    final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            final SocketChannel channel = (SocketChannel) key.channel();
                            final ByteBuffer buffer = ByteBuffer.allocate(16);
                            channel.read(buffer);
                            buffer.flip();
                            log.info(StandardCharsets.UTF_8.decode(buffer).toString());
                            buffer.clear();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
