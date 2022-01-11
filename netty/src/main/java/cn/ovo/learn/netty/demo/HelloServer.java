package cn.ovo.learn.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {

    public static void main(String[] args) {
        // 1.定义服务器启动器
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 2.添加事件组
        bootstrap.group(new NioEventLoopGroup());
        // 3.选择Server Channel的实现
        bootstrap.channel(NioServerSocketChannel.class);
        // 4.定义事件处理器
        bootstrap.childHandler(
                // 5.初始化和客户端进行数据读写的Channel
                new ChannelInitializer<NioSocketChannel>() {
                    // 与客户端建立连接后调用该方法
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 6.添加具体的handler
                        // 6.1 将bytebuf转换为字符串的处理器
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        // 6.2 自定义处理器
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }

                        });
                    }
                });
        // 7. 绑定端口
        bootstrap.bind(8080);
    }
}
