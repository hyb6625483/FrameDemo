package cn.ovo.learn.netty.rpc.message;

import cn.ovo.learn.netty.chat.message.Message;
import lombok.Data;
import lombok.ToString;

/**
 * @author yihang
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    public RpcResponseMessage() {
        super();
    }

    public RpcResponseMessage(Object obj) {
        this.returnValue = obj;
    }

    public RpcResponseMessage(Exception e) {
        this.exceptionValue = e;
    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
