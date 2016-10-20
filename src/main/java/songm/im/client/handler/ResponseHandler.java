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

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import songm.im.client.entity.Message;
import songm.im.client.entity.Protocol;
import songm.im.client.entity.Result;
import songm.im.client.event.ActionEvent.EventType;
import songm.im.client.event.ActionListenerManager;
import songm.im.client.utils.JsonUtils;

public class ResponseHandler implements Handler {

    @Override
    public int operation() {
        return 0;
    }

    @Override
    public void action(ActionListenerManager listenerManager, Protocol pro) {
        Result<?> res = null;
        Type type = null;

        if (pro.getOperation() == Operation.MSG_SEND.getValue()) {
            type = new TypeToken<Result<Message>>() {
            }.getType();
        }

        res = JsonUtils.fromJson(pro.getBody(), type);
        listenerManager.trigger(EventType.RESPONSE, res, pro.getSequence());
    }

}
