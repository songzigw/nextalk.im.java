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
package songm.im.client.handler;

import songm.im.client.IMClient.Operation;
import songm.im.client.entity.Message;
import songm.im.client.entity.Protocol;
import songm.im.client.event.ActionListenerManager;
import songm.im.client.event.ActionEvent.EventType;
import songm.im.client.utils.JsonUtils;

public class MessageHandler implements Handler {

    @Override
    public int operation() {
        return Operation.MESSAGE.getValue();
    }

    @Override
    public void action(ActionListenerManager listenerManager, Protocol pro) {
        Message message = JsonUtils.fromJson(pro.getBody(), Message.class);
        listenerManager.trigger(EventType.MESSAGE, message, null);
    }

}
