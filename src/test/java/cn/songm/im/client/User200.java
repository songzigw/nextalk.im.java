package cn.songm.im.client;

import cn.songm.im.codec.model.Message;

public class User200 {

    private static String ip = "127.0.0.1";
    private static int port = 17181;
    private static String tokenId = "80LGKEGEHQLF7N8L";
    private static String uid = "200";
    
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
    }
}
