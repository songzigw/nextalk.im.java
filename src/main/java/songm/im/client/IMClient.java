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

import songm.im.client.event.ClientListener;

/**
 * 聊天客户端
 *
 * @author zhangsong
 * @since 0.1, 2016-7-29
 * @version 0.1
 * 
 */
public interface IMClient {

    public void addListener(ClientListener listener);

    public void connect(String token) throws IMException;

    public void disconnect();

    public static enum Operation {
        /** 连接授权 */
        CONN_AUTH(1),

        /** 消息发生 */
        MESSAGE(3),
        /** 消息发送到服务器 */
        MSG_SEND(4);

        private final int value;

        private Operation(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Operation getInstance(int v) {
            for (Operation type : Operation.values()) {
                if (type.getValue() == v) {
                    return type;
                }
            }
            return null;
        }
    }
}
