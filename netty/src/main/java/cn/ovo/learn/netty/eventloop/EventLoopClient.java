package cn.ovo.learn.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);
        Channel channel = channelFuture.sync().channel();
        System.out.println(channel);
        channel.writeAndFlush("Hello");
        System.out.println("waiting...");

//        channelFuture.addListener((ChannelFutureListener) future -> {
//            Channel channel = future.channel();
//            System.out.println(channel);
//            channel.writeAndFlush("Hello");
//        });
//        System.out.println("waiting...");

        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            worker.shutdownGracefully();
        });

    }
}
