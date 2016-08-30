package songm.im.client;

import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Session;
import songm.im.client.event.ClientListener;

public class IMClientTest {

    public static void main(String[] args) {
        IMClientImpl client = IMClientImpl.init("127.0.0.1", 9090);

        client.addListener(new ClientListener() {

            @Override
            public void onDisconnected(ErrorCode errorCode) {
                System.out.println(1);
            }

            @Override
            public void onConnecting() {
                System.out.println(2);
            }

            @Override
            public void onConnected(Session Session) {
                System.out.println(3);
            }
        });

        try {
            client.connect("zhangsong");
        } catch (IMException e) {
            e.printStackTrace();
        }
        
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //client.disconnect();
    }

}
