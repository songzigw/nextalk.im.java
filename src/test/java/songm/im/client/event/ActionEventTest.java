package songm.im.client.event;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import songm.im.client.entity.Entity;
import songm.im.client.entity.Message;
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
        Session ses = new Session();

        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        assertThat(event.getSource(), is(EventType.CONNECTING));
                        assertThat(event.getData(), is(ses));
                    }
                });
        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        assertThat(event.getSource(), is(EventType.CONNECTED));
                        assertThat(event.getData(), is(ses));
                    }
                });
        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        assertThat(event.getSource(),
                                is(EventType.DISCONNECTED));
                        assertThat(event.getData(), is(ses));
                    }
                });

        listenerManager.trigger(EventType.CONNECTING, ses, null);
        listenerManager.trigger(EventType.CONNECTED, ses, null);
        listenerManager.trigger(EventType.DISCONNECTED, ses, null);
    }

    @Test
    public void tesRequest() {
        Long seq = new Date().getTime();

        // 相应返回的结果对象
        Entity ent = new Entity();

        listenerManager.addListener(EventType.RESPONSE, new ActionListener() {

            @Override
            public Long getSequence() {
                return seq;
            }

            @Override
            public void actionPerformed(ActionEvent event) {
                assertThat(event.getSource(), is(EventType.RESPONSE));
                assertThat(event.getData(), is(ent));
            }
        });

        listenerManager.trigger(EventType.RESPONSE, ent, seq);
    }

    @Test
    public void testMessageReceive() {
        Message msg = new Message();

        listenerManager.addListener(EventType.MESSAGE, new AbstractListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                assertThat(event.getSource(), is(EventType.MESSAGE));
                assertThat(event.getData(), is(msg));
            }
        });

        listenerManager.trigger(EventType.MESSAGE, msg, null);
    }
}
