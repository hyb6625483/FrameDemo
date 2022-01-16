package cn.ovo.learn.netty.rpc.server;

import cn.ovo.learn.netty.chat.protocol.MessageCodecSharable;
import cn.ovo.learn.netty.chat.protocol.ProcotolFrameDecoder;
import cn.ovo.learn.netty.rpc.handler.RpcRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcServer {
    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    private static final MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
    private static final RpcRequestMessageHandler RPC_HANDLER = new RpcRequestMessageHandler();

    public static void main(String[] args) {
        // 定义监听连接的线程
        EventLoopGroup boss = new NioEventLoopGroup();
        // 定义处理连接的线程
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 定义启动器
            ServerBootstrap server = new ServerBootstrap();
            // 添加事件处理线程
            server.group(boss, worker);
            // 定义 Channel
            server.channel(NioServerSocketChannel.class);
            // 定义事件处理器
            server.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel sc) throws Exception {
                    // 黏包、半包处理器
                    sc.pipeline().addLast(new ProcotolFrameDecoder());
                    // 日志处理器
                    sc.pipeline().addLast(LOGGING_HANDLER);
                    // 消息编、解码处理器
                    sc.pipeline().addLast(MESSAGE_CODEC);
                    // RPC处理器
                    sc.pipeline().addLast(RPC_HANDLER);
                }
            });
            final Channel channel = server.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
