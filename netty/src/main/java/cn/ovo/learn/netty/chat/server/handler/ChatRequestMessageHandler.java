package cn.ovo.learn.netty.chat.server.handler;

import cn.ovo.learn.netty.chat.message.ChatRequestMessage;
import cn.ovo.learn.netty.chat.message.ChatResponseMessage;
import cn.ovo.learn.netty.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        // 获取指定用户的channel
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            // 在线
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            // 不在线
            ctx.writeAndFlush(new ChatResponseMessage(false, "用户【" + to + "】不存在或者不在线"));
        }
    }
}
