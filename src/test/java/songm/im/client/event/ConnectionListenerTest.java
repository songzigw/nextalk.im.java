package songm.im.client.event;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Result;
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
        Result<Session> res = new Result<Session>();
        Session ses = new Session();
        res.setData(ses);

        final ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void onDisconnected(ErrorCode errorCode) {
                assertThat(errorCode.name(), is(res.getErrorCode()));
            }

            @Override
            public void onConnecting() {
                assertThat(1, is(1));
            }

            @Override
            public void onConnected(Session session) {
                assertThat(session, is(ses));
            }
        };

        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        connectionListener.onConnecting();
                    }
                });
        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        Session session = event.getResult().getData();
                        connectionListener.onConnected(session);
                    }
                });
        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        ErrorCode errorCode = ErrorCode.valueOf(event.getResult().getErrorCode());
                        connectionListener.onDisconnected(errorCode);
                    }
                });

        listenerManager.trigger(EventType.CONNECTING, res, null);
        listenerManager.trigger(EventType.CONNECTED, res, null);
        res.setErrorCode(ErrorCode.CONN_ERROR.name());
        listenerManager.trigger(EventType.DISCONNECTED, res, null);
    }
}
