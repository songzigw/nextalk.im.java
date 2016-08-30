package songm.im.client.event;

import java.util.EventListener;

import songm.im.client.entity.Message;

public interface MessageListener extends EventListener {

    /**
     * 收到消息时
     * 
     * @param message
     */
    public abstract void onReceived(Message message);
}
