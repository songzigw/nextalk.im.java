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

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import songm.im.client.entity.Protocol;
import songm.im.client.entity.Session;
import songm.im.client.event.ActionEvent.EventType;
import songm.im.client.event.ActionListenerManager;
import songm.im.client.handler.Handler;
import songm.im.client.handler.HandlerManager;
import songm.im.client.handler.Handler.Operation;
import songm.im.client.utils.JsonUtils;

/**
 * 事件消息处理
 *
 * @author zhangsong
 * @since 0.1, 2016-7-29
 * @version 0.1
 * 
 */
@ChannelHandler.Sharable
public class IMClientHandler extends SimpleChannelInboundHandler<Protocol> {

    private static final Logger LOG = LoggerFactory.getLogger(IMClientHandler.class);

    private ActionListenerManager listenerManager;
    private HandlerManager handlerManager;
    private final Session session;

    public IMClientHandler(ActionListenerManager listenerManager, Session session) {
        this.listenerManager = listenerManager;
        this.handlerManager = new HandlerManager();
        this.session = session;
    }

    private void authorization(ChannelHandlerContext ctx) {
        Protocol proto = new Protocol();
        proto.setOperation(Operation.CONN_AUTH.getValue());
        proto.setBody(JsonUtils.toJsonBytes(session, Session.class));

        ctx.channel().writeAndFlush(proto);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        authorization(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Protocol pro) throws Exception {
        Handler handler = handlerManager.find(pro.getOperation());
        if (handler != null) {
            handler.action(listenerManager, pro);
        } else {
            LOG.warn("Not found handler: " + pro.getOperation());
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOG.debug("HandlerRemoved", ctx);
        listenerManager.trigger(EventType.DISCONNECTED, null, null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("ExceptionCaught", cause);
    }
}
