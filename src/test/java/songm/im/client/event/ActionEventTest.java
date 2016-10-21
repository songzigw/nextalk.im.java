package songm.im.client.event;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import songm.im.client.entity.Message;
import songm.im.client.entity.Result;
import songm.im.client.entity.Session;
import songm.im.client.event.ActionEvent.EventType;

/**
 * 各种事件测试
 * 
 * @author zhangsong
 *
 */
public class ActionEventTest {

    private final ActionListenerManager listenerManager;
    {
        listenerManager = new ActionListenerManager();
    }

    @Test
    public void testConnection() {
        Result<Session> res = new Result<Session>();
        Session ses = new Session();
        res.setData(ses);

        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        assertThat(event.getSource(), is(EventType.CONNECTING));
                        assertThat(event.getResult().getData(), is(ses));
                    }
                });
        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        assertThat(event.getSource(), is(EventType.CONNECTED));
                        assertThat(event.getResult().getData(), is(ses));
                    }
                });
        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener<Session>() {
                    @Override
                    public void actionPerformed(ActionEvent<Session> event) {
                        assertThat(event.getSource(),
                                is(EventType.DISCONNECTED));
                        assertThat(event.getResult().getData(), is(ses));
                    }
                });

        listenerManager.trigger(EventType.CONNECTING, res, null);
        listenerManager.trigger(EventType.CONNECTED, res, null);
        listenerManager.trigger(EventType.DISCONNECTED, res, null);
    }

    @Test
    public void tesRequest() {
        Long seq = new Date().getTime();

        // 相应返回的结果对象
        Result<Object> res = new Result<Object>();

        listenerManager.addListener(EventType.RESPONSE, new ActionListener<Object>() {

            @Override
            public Long getSequence() {
                return seq;
            }

            @Override
            public void actionPerformed(ActionEvent<Object> event) {
                assertThat(event.getSource(), is(EventType.RESPONSE));
                assertThat(event.getResult(), is(res));
            }
        });

        listenerManager.trigger(EventType.RESPONSE, res, seq);
    }

    @Test
    public void testMessageReceive() {
        Result<Message> res = new Result<Message>();
        Message msg = new Message();
        res.setData(msg);

        listenerManager.addListener(EventType.MESSAGE, new AbstractListener<Message>() {
            @Override
            public void actionPerformed(ActionEvent<Message> event) {
                assertThat(event.getSource(), is(EventType.MESSAGE));
                assertThat(event.getResult().getData(), is(msg));
            }
        });

        listenerManager.trigger(EventType.MESSAGE, res, null);
    }
}
