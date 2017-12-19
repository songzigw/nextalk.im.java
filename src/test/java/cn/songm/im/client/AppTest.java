package cn.songm.im.client;

import cn.songm.im.client.IMCallback;
import cn.songm.im.client.IMClient;
import cn.songm.im.codec.AckListener;
import cn.songm.im.codec.model.Message;
import cn.songm.im.codec.model.Message.Mtype;
import cn.songm.im.codec.model.TextMessage;

public class AppTest {

    public static void main(String[] args) throws InterruptedException {
        IMClient c = new IMClient("127.0.0.1", 17181);
        c.setCallback(new IMCallback() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message);
            }

            @Override
            public void onDisconnected() {
                System.out.println("disconnected");
            }
        });
        c.connect("M1EI31794IGG1NA6", "100");

        Message m = new Message();
        m.setType(Mtype.TEXT);
        m.setTo("200");
        m.setJbody(new TextMessage("这是一个好消息"));
        c.send(m, new AckListener<Message>() {
            @Override
            public void onSuccess(Message data) {
                System.out.println(data);
            }

            @Override
            public void onError(int eCode, String eMsg) {
                System.out.println(eMsg);
            }
        });
    }
}
