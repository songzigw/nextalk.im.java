package songm.im.client.event;

import java.util.EventObject;

public class ActionEvent extends EventObject {

    private static final long serialVersionUID = -6605811937291134933L;

    private Long sequence;

    private Object data;

    public ActionEvent(EventType source, Object data) {
        super(source);
        this.data = data;
    }

    @Override
    public EventType getSource() {
        return (EventType) super.getSource();
    }

    public Object getData() {
        return data;
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
