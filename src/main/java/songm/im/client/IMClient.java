/*
 * Copyright [2016] [zhangsong <songm.cn>].
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package songm.im.client;

import songm.im.client.entity.Entity;
import songm.im.client.entity.Message;
import songm.im.client.event.ConnectionListener;
import songm.im.client.event.ResponseListener;

/**
 * 聊天客户端
 *
 * @author zhangsong
 * @since 0.1, 2016-7-29
 * @version 0.1
 * 
 */
public interface IMClient {

    /** 连接断开 */
    public static final int DISCONNECTED = 0;
    /** 连接上了 */
    public static final int CONNECTED = 1;
    /** 正在连接 */
    public static final int CONNECTING = 2;

    public int getConnState();
    
    public void addConnectionListener(ConnectionListener listener);

    public void connect(String token) throws IMException;

    public void disconnect();
    
    public void sendMessage(Message message, ResponseListener<Entity> response);

}
