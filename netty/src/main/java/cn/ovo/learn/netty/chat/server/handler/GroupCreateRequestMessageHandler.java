package cn.ovo.learn.netty.chat.server.handler;

import cn.ovo.learn.netty.chat.message.GroupCreateRequestMessage;
import cn.ovo.learn.netty.chat.message.GroupCreateResponseMessage;
import cn.ovo.learn.netty.chat.server.session.Group;
import cn.ovo.learn.netty.chat.server.session.GroupSession;
import cn.ovo.learn.netty.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        // 群管理器
        GroupSession session = GroupSessionFactory.getGroupSession();
        Group group = session.createGroup(groupName, msg.getMembers());
        if (group == null) {
            // 发生创建成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, "群组【" + groupName + "】创建成功"));
            // 发送拉群消息
            List<Channel> channels = session.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入【" + groupName + "】群组"));
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, "群组【" + groupName + "】已存在"));
        }
    }
}
