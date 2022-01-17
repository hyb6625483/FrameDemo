package cn.ovo.learn.netty.rpc.client;

import cn.ovo.learn.netty.chat.protocol.MessageCodecSharable;
import cn.ovo.learn.netty.chat.protocol.ProcotolFrameDecoder;
import cn.ovo.learn.netty.protocol.SequenceIdGenerator;
import cn.ovo.learn.netty.rpc.message.RpcRequestMessage;
import cn.ovo.learn.netty.rpc.message.RpcResponseMessage;
import cn.ovo.learn.netty.rpc.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientManager {
    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    private static final MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

    private static volatile Channel channel;
    private static final Object LOCK = new Object();

    private static final Map<Integer, Promise<Object>> PROMISE_MAP = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        HelloService service = getProxyService(HelloService.class);
        System.out.println(service.sayHello("张三"));
        System.out.println(service.sayHello("李四"));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        return (T) Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 构建请求消息
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args);
            // 发送消息
            channel = getChannel();
            channel.writeAndFlush(msg);
            // 获取消息
            Promise<Object> promise = new DefaultPromise<>(channel.eventLoop());
            PROMISE_MAP.put(sequenceId, promise);
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            } else {
                throw new RuntimeException(promise.cause());
            }
        });
    }

    /**
     * 获取Channel, 双重检测锁
     *
     * @return channel
     */
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    /**
     * 初始化 Channel
     */
    private static void initChannel() {
        EventLoopGroup group = new NioEventLoopGroup();
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
                        Promise<Object> promise = PROMISE_MAP.remove(msg.getSequenceId());
                        if (promise != null) {
                            if (msg.getExceptionValue() != null) {
                                promise.setFailure(msg.getExceptionValue());
                            } else {
                                promise.setSuccess(msg.getReturnValue());
                            }
                        }
                    }
                });
            }
        });
        try {
            channel = client.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
