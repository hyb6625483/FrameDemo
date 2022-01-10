package cn.ovo.learn.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SocketServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("Boss-");
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
        // 处理事件
        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    clientChannel.configureBlocking(false);
                }
            }
        }
    }
}
