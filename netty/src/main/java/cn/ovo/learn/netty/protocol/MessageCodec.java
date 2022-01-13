package cn.ovo.learn.netty.protocol;

import cn.ovo.learn.netty.protocol.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.*;
import java.util.List;

public class MessageCodec extends ByteToMessageCodec<AbstractMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) throws Exception {
        // 1.定义魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2.定义版本号
        out.writeByte(1);
        // 3.定义序列化方式
        out.writeByte(0);
        // 4.定义指令的类型
        out.writeByte(1);
        // 5.定义序号
        out.writeInt(4);
        // 字节对齐，2^n
        out.writeByte(0xff);
        // 获取数据
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(byteStream);
        os.writeObject(msg);
        byte[] bytes = byteStream.toByteArray();
        // 6.指定数据长度
        out.writeInt(bytes.length);
        // 7.写入内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        if (serializerType == 0) {
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
            AbstractMessage msg = (AbstractMessage) is.readObject();
            out.add(msg);
        }
    }
}
