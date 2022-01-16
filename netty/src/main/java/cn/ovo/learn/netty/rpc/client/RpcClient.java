package cn.ovo.learn.netty.rpc.client;

import cn.ovo.learn.netty.chat.protocol.MessageCodecSharable;
import cn.ovo.learn.netty.chat.protocol.ProcotolFrameDecoder;
import cn.ovo.learn.netty.rpc.message.RpcRequestMessage;
import cn.ovo.learn.netty.rpc.message.RpcResponseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcClient {
    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    private static final MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group);
            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {

                    // 黏包、半包处理器
                    channel.pipeline().addLast(new ProcotolFrameDecoder());
                    // 日志处理器
                    channel.pipeline().addLast(LOGGING_HANDLER);
                    // 消息编、解码处理器
                    channel.pipeline().addLast(MESSAGE_CODEC);
                    channel.pipeline().addLast(new SimpleChannelInboundHandler<RpcResponseMessage>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channel, RpcResponseMessage msg) throws Exception {
                            if (msg.getExceptionValue() != null) {
                                msg.getExceptionValue().printStackTrace();
                            } else {
                                System.out.println(msg.getReturnValue());
                            }
                        }
                    });
                }
            });
            final Channel channel = client.connect("localhost", 8080).sync().channel();
            channel.writeAndFlush(new RpcRequestMessage(1, "cn.ovo.learn.netty.rpc.service.HelloService",
                    "sayHello", String.class, new Class[]{String.class}, new String[]{"张三"}));
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
