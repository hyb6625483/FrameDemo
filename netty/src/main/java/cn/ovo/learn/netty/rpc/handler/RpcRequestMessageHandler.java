package cn.ovo.learn.netty.rpc.handler;

import cn.ovo.learn.netty.rpc.message.RpcRequestMessage;
import cn.ovo.learn.netty.rpc.message.RpcResponseMessage;
import cn.ovo.learn.netty.rpc.service.ServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        try {
            // 获取服务实例
            final Object service = ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            // 获取调用的方法
            final Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            // 反射调用方法
            final Object result = method.invoke(service, msg.getParameterValue());
            ctx.writeAndFlush(new RpcResponseMessage(result));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.writeAndFlush(new RpcResponseMessage(e));
        }
    }
}