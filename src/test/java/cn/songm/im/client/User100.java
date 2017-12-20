package cn.songm.im.client;

import cn.songm.im.codec.AckListener;
import cn.songm.im.codec.model.Message;
import cn.songm.im.codec.model.TextMessage;
import cn.songm.im.codec.model.Message.Mtype;

public class User100 {

    private static String ip = "127.0.0.1";
    private static int port = 17181;
    private static String tokenId = "DT2QPI551FAO4DZU";
    private static String uid = "100";
    
    public static void main(String[] args) throws InterruptedException {
        // 创建客户端
        IMClient c = new IMClient(ip, port);
        // 设置回调
        c.setCallback(new IMCallback() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message);
            }

            @Override
            public void onDisconnect() {
                System.out.println("disconnected");
            }
        });
        // 连接IM服务
        c.connect(tokenId, uid);

        // 创建文本消息
        Message m = new Message();
        m.setType(Mtype.TEXT);
        m.setTo("200");
        m.setJbody(new TextMessage("这是一个好消息"));
        // 发送消息
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
        
        // 关闭连接
        c.disconnect();
    }
}
