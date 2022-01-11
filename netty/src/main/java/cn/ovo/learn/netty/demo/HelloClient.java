package cn.ovo.learn.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        // 创建客户端启动器
        Bootstrap bootstrap = new Bootstrap();
        // 添加事件组
        bootstrap.group(new NioEventLoopGroup());
        // 指定Channel
        bootstrap.channel(NioSocketChannel.class);
        // 定义处理器
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            // 连接建立后调用该方法
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                // 对消息进行编码
                nioSocketChannel.pipeline().addLast(new StringEncoder());
            }
        });
        // 建立连接
        bootstrap.connect("localhost", 8080)
                // 阻塞方法直到连接建立成功
                .sync()
                // 连接的channel对象
                .channel()
                // 发送消息
                .writeAndFlush("hello world");
    }
}
