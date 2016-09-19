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

import java.util.HashMap;
import java.util.Map;

/**
 * 事件处理管理器
 * 
 * @author zhannsong
 *
 */
public class HandlerManager {

    private Map<Integer, Handler> ops = new HashMap<Integer, Handler>();
    {
        ops.put(0, new ResponseHandler());
        ops.put(Handler.Type.CONN_AUTH.getValue(), new ConnAuthHandler());
        ops.put(Handler.Type.MESSAGE.getValue(), new MessageHandler());
    }

    public Handler find(Integer op) {
        Handler h = ops.get(op);
        if (h != null) {
            return h;
        }

        for (Handler.Type o : Handler.Type.values()) {
            if (o.getValue() == op) {
                return ops.get(0);
            }
        }
        return null;
    }
}
