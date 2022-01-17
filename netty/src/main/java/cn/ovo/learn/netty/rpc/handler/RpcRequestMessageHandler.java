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
        RpcResponseMessage message = new RpcResponseMessage();
        message.setSequenceId(msg.getSequenceId());
        message.setMessageType(msg.getMessageType());
        try {
            // 获取服务实例
            Object service = ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            // 获取调用的方法
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            // 反射调用方法
            Object result = method.invoke(service, msg.getParameterValue());
            message.setReturnValue(result);
        } catch (Exception e) {
            e.printStackTrace();
            message.setExceptionValue(new Exception(e.getCause().getMessage()));
        }
        ctx.writeAndFlush(message);
    }
}