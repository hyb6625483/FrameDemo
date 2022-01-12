package cn.ovo.learn.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = (ByteBuf) msg;
                                log.info("接收到服务器响应的消息：{} ---> {}", buffer.toString(StandardCharsets.UTF_8), ctx.channel());
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                }).connect("localhost", 8080).sync().channel();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if ("q".equals(s)) {
                break;
            }
            channel.writeAndFlush(s);
        }
        channel.close();
    }
}
