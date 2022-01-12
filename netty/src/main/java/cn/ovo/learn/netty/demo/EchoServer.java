package cn.ovo.learn.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class EchoServer {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup workers = new NioEventLoopGroup(4);

        new ServerBootstrap()
                .group(boss, workers)
                // 全局设置
//                .option(ChannelOption.SO_RCVBUF, 10)
                // 连接中的设置
//                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16,16,16))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 黏包半包解决方式1：定长解码器
//                        ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                        // 黏包半包解决方式2：换行解码器
//                        ch.pipeline().addLast(new LineBasedFrameDecoder(10));
                        // 黏包半包解决方式3：TLC (Type Length Content)
//                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4));
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                ByteBuf buffer = (ByteBuf) msg;
                                log.info("服务端发送消息: {} ---> {}", buffer.toString(StandardCharsets.UTF_8), ctx.channel());
                                super.write(ctx, msg, promise);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                log.info("客户端建立连接 ---> {}", ctx.channel());
                                super.channelRegistered(ctx);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = (ByteBuf) msg;
                                log.info("接收到客户端的消息：{} ---> {}", buffer.toString(StandardCharsets.UTF_8), ctx.channel());
                                ctx.writeAndFlush(msg);
                            }
                        });
                    }

                }).bind(8080);

    }
}
