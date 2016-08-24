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
package songm.im.client.event;

import java.util.EventListener;

import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Session;

/**
 * 后台客户端事件监听器
 *
 * @author  zhangsong
 * @since   0.1, 2016-8-2
 * @version 0.1
 *
 */
public abstract class ClientListener implements EventListener {

    /**
     * 当正在连接时，被触发。
     */
    public abstract void onConnecting();

    /**
     * 当连接成功时，被触发。
     * @param Session 
     */
    public abstract void onConnected(Session Session);

    /**
     * 当连接断开时，被触发。
     * @param errorCode
     */
    public abstract void onDisconnected(ErrorCode errorCode);

}
