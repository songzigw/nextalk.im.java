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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Entity;
import songm.im.client.entity.Message;
import songm.im.client.entity.Protocol;
import songm.im.client.entity.Session;
import songm.im.client.event.AbstractListener;
import songm.im.client.event.ActionEvent;
import songm.im.client.event.ActionEvent.EventType;
import songm.im.client.event.ActionListener;
import songm.im.client.event.ActionListenerManager;
import songm.im.client.event.ConnectionListener;
import songm.im.client.event.ResponseListener;
import songm.im.client.handler.Handler.Operation;
import songm.im.client.utils.JsonUtils;

/**
 * 后台客户端的实现
 *
 * @author zhangsong
 * @since 0.1, 2016-7-29
 * @version 0.1
 * 
 * @see #IMClient
 * 
 */
public class IMClientImpl implements IMClient {

    private static final Logger LOG = LoggerFactory
            .getLogger(IMClientImpl.class);

    private final String host;
    private final int port;

    private final ActionListenerManager listenerManager;
    private final EventLoopGroup group;

    private IMClientInitializer clientInit;
    private ChannelFuture channelFuture;
    private ConnectionListener connectionListener;
    
    private int connState;

    private IMClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
        this.listenerManager = new ActionListenerManager();
        this.group = new NioEventLoopGroup();
        this.session = new Session();
        this.init();
    }

    private final Session session;
    private void init() {
        IMClientImpl self = this;
        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        self.connState = CONNECTING;
                        if (connectionListener != null) {
                            connectionListener.onConnecting();
                        }
                    }
                });
        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        self.connState = CONNECTED;
                        Session ses = (Session) event.getData();
                        session.setSessionId(ses.getSessionId());
                        if (connectionListener != null) {
                            connectionListener.onConnected(session);
                        }
                    }
                });
        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        self.connState = DISCONNECTED;
                        // (Session) event.getData();
                        if (connectionListener != null) {
                            connectionListener.onDisconnected(ErrorCode
                                    .valueOf(session.getErrorCode()));
                        }
                    }
                });
    }

    private static IMClientImpl instance;
    public static IMClientImpl getInstance() {
        if (instance == null) {
            throw new NullPointerException("IMClient not init");
        }
        return instance;
    }

    public static IMClientImpl init(String host, int port) {
        return new IMClientImpl(host, port);
    }

    public Session getBacstage() {
        return session;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void connect(String token) throws IMException {
        if (connState == CONNECTED || connState == CONNECTING) {
            return;
        }

        LOG.debug("Connecting SongmIM Server Host:{} Port:{}", host, port);
        session.setTokenId(token);
        this.clientInit = new IMClientInitializer(listenerManager, session);
        listenerManager.trigger(EventType.CONNECTING, token, null);

        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.handler(clientInit);
        b.remoteAddress(host, port);

        try {
            // 与服务器建立连接
            channelFuture = b.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Connect failure", e);
            throw new IMException(ErrorCode.CONN_ERROR, "connect", e);
        } finally {
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    @Override
    public void sendMessage(Message message, ResponseListener<Entity> response) {
        Protocol proto = new Protocol();
        proto.setOperation(Operation.MSG_SEND.getValue());
        proto.setSequence(new Date().getTime());
        proto.setBody(JsonUtils.toJson(message, Message.class).getBytes());

        listenerManager.addListener(EventType.RESPONSE, new ActionListener() {

            private Long sequence = proto.getSequence();

            @Override
            public Long getSequence() {
                return sequence;
            }

            @Override
            public void actionPerformed(ActionEvent event) {
                if (response == null) {
                    return;
                }
                Entity ent = (Entity) event.getData();
                if (ent.getSucceed()) {
                    response.onSuccess(ent);
                } else {
                    response.onError(ErrorCode.valueOf(ent.getErrorCode()));
                }
            }
        });

        channelFuture.channel().writeAndFlush(proto);
    }

    @Override
    public int getConnState() {
        return this.connState;
    }
}
