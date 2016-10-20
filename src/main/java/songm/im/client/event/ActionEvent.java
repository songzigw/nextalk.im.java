package songm.im.client.event;

import java.util.EventObject;

import songm.im.client.entity.Result;

public class ActionEvent<T> extends EventObject {

    private static final long serialVersionUID = -6605811937291134933L;

    private Long sequence;

    private Result<T> result;

    public ActionEvent(EventType source, Result<T> result, Long sequence) {
        super(source);
        this.result = result;
        this.sequence = sequence;
    }

    @Override
    public EventType getSource() {
        return (EventType) super.getSource();
    }

    public Result<T> getResult() {
        return result;
    }

    public Long getSequence() {
        return sequence;
    }

    public static enum EventType {
        /** 请求后应答 */
        RESPONSE,
        
        /** 正在连接 */
        CONNECTING,
        /** 连接成功 */
        CONNECTED,
        /** 断开连接 */
        DISCONNECTED,
        
        /** 消息收到 */
        MESSAGE;
    }
}
