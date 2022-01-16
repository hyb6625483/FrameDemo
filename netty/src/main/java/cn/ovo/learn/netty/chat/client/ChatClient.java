package cn.ovo.learn.netty.chat.client;

import cn.ovo.learn.netty.chat.message.*;
import cn.ovo.learn.netty.chat.protocol.MessageCodecSharable;
import cn.ovo.learn.netty.chat.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        Executor executor = Executors.newSingleThreadExecutor();
        AtomicBoolean loginSuccess = new AtomicBoolean(false);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
            MessageCodecSharable messageCodec = new MessageCodecSharable();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IdleStateHandler(0,3,0));
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.WRITER_IDLE) {
                                ctx.writeAndFlush(new PingMessage());
                            }
                        }
                    });
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
//                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(messageCodec);
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            executor.execute(() -> {
                                Scanner scanner = new Scanner(System.in);
                                System.out.print("请输入用户名：");
                                String username = scanner.nextLine();
                                System.out.print("请输入密码：");
                                String password = scanner.nextLine();
                                ctx.writeAndFlush(new LoginRequestMessage(username, password));
                                try {
                                    countDownLatch.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (loginSuccess.get()) {
                                    while (true) {
                                        System.out.println("==================================");
                                        System.out.println("send [username] [content]");
                                        System.out.println("gsend [group name] [content]");
                                        System.out.println("gcreate [group name] [m1,m2,m3...]");
                                        System.out.println("gmembers [group name]");
                                        System.out.println("gjoin [group name]");
                                        System.out.println("gquit [group name]");
                                        System.out.println("quit");
                                        System.out.println("==================================");
                                        String command = scanner.nextLine();
                                        String[] s = command.split(" ");
                                        switch (s[0]) {
                                            case "send":
                                                ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                                break;
                                            case "gsend":
                                                ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                                break;
                                            case "gcreate":
                                                Set<String> members = new HashSet<>(Arrays.asList(s[2].split(",")));
                                                members.add(username);
                                                ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], members));
                                                break;
                                            case "gmembers":
                                                ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                                break;
                                            case "gjoin":
                                                ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                                break;
                                            case "gquit":
                                                ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                                break;
                                            case "quit":
                                                return;
                                            default:
                                                break;
                                        }
                                    }
                                }
                                ctx.channel().close();
                            });
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage response = (LoginResponseMessage) msg;
                                log.info(response.getReason());
                                loginSuccess.set(response.isSuccess());
                                countDownLatch.countDown();
                            } else if (msg instanceof ChatResponseMessage) {
                                ChatResponseMessage message = (ChatResponseMessage) msg;
                                log.info("接收到【{}】的消息：{}", message.getFrom(), message.getContent());
                            } else {
                                log.info("{}", msg);
                            }
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

}
