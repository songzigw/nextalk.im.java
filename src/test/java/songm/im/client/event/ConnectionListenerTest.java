package songm.im.client.event;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Session;
import songm.im.client.event.ActionEvent.EventType;

/**
 * 客户端事件测试
 * 
 * @author zhangsong
 *
 */
public class ConnectionListenerTest {

    private final ActionListenerManager listenerManager;
    {
        listenerManager = new ActionListenerManager();
    }

    @Test
    public void testClientEvent() {
        Session ses = new Session();

        final ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void onDisconnected(ErrorCode errorCode) {
                assertThat(errorCode.name(), is(ses.getErrorCode()));
            }

            @Override
            public void onConnecting() {
            }

            @Override
            public void onConnected(Session session) {
                assertThat(session, is(ses));
            }
        };

        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        connectionListener.onConnecting();
                    }
                });
        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        connectionListener.onConnecting();
                    }
                });
        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        connectionListener.onConnecting();
                    }
                });

        listenerManager.trigger(EventType.CONNECTING, ses, null);
        listenerManager.trigger(EventType.CONNECTED, ses, null);
        listenerManager.trigger(EventType.DISCONNECTED, ses, null);
    }
}
