package cn.ovo.learn.netty.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisClient {

    /**
     * protocol
     * <p>
     * set name zhangsan
     * *3
     * $3
     * set
     * $4
     * name
     * $8
     * zhangsan
     * </p>
     * @param args
     */
    public static void main(String[] args) {
        final byte[] line = {13, 10};
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        try {
            client.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    // Netty提供的Redis协议封装
                    ch.pipeline().addLast(new RedisEncoder());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buffer = ctx.alloc().buffer();
                            buffer.writeBytes("*3".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("$3".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("set".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("$4".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("name".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("$8".getBytes());
                            buffer.writeBytes(line);
                            buffer.writeBytes("zhangsan".getBytes());
                            buffer.writeBytes(line);
                            ctx.writeAndFlush(buffer);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.info("Redis响应数据：{}", buf.toString());
                            super.channelRead(ctx, msg);
                        }
                    });
                }
            }).connect("192.168.1.108", 6370).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
