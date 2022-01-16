package cn.ovo.learn.netty.chat.server.handler;

import cn.ovo.learn.netty.chat.message.GroupChatRequestMessage;
import cn.ovo.learn.netty.chat.message.GroupChatResponseMessage;
import cn.ovo.learn.netty.chat.server.session.GroupSessionFactory;
import cn.ovo.learn.netty.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(msg.getGroupName());
        ctx.writeAndFlush(new GroupChatResponseMessage(true, "发送成功"));
        for (Channel channel : channels) {
            if (msg.getFrom().equals(SessionFactory.getSession().getUser(channel))) {
                continue;
            }
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
