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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import songm.im.client.IMException.ErrorCode;
import songm.im.client.entity.Message;
import songm.im.client.entity.Protocol;
import songm.im.client.entity.Session;
import songm.im.client.event.AbstractListener;
import songm.im.client.event.ActionEvent;
import songm.im.client.event.ActionListener;
import songm.im.client.event.ActionEvent.EventType;
import songm.im.client.event.ActionListenerManager;
import songm.im.client.event.ClientListener;
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
    private final IMClientInitializer clientInit;
    private ChannelFuture channelFuture;
    private ClientListener listener;

    private static IMClientImpl instance;

    private IMClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.listenerManager = new ActionListenerManager();
        this.clientInit = new IMClientInitializer(listenerManager);
        this.init();
    }

    private Session session;

    private void init() {
        listenerManager.addListener(EventType.CONNECTING,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (listener != null) {
                            listener.onConnecting();
                        }
                    }
                });

        listenerManager.addListener(EventType.CONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        session = (Session) event.getData();
                        if (listener != null) {
                            listener.onConnected(session);
                        }
                    }
                });

        listenerManager.addListener(EventType.DISCONNECTED,
                new AbstractListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        session = (Session) event.getData();
                        if (listener != null) {
                            listener.onDisconnected(ErrorCode.valueOf(session
                                    .getErrorCode()));
                        }
                    }
                });
    }

    public static IMClientImpl getInstance() {
        if (instance == null) {
            throw new NullPointerException("IMClient not init");
        }
        return instance;
    }

    public static IMClientImpl init(String host, int port) {
        instance = new IMClientImpl(host, port);
        return instance;
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
        LOG.info("Connecting SongmIM Server... Host:{} Port:{}", host, port);

        listenerManager.trigger(EventType.CONNECTING, token, null);

        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.remoteAddress(host, port);
        b.handler(clientInit);

        try {
            // 与服务器建立连接
            channelFuture = b.connect().sync();
            this.auth(token);
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

    private void auth(String token) {
        session = new Session();
        session.setTokenId(token);

        Protocol proto = new Protocol();
        proto.setOperation(Operation.CONN_AUTH.getValue());
        proto.setBody(JsonUtils.toJson(session).getBytes());

        channelFuture.channel().writeAndFlush(proto);
    }

    @Override
    public void addListener(ClientListener listener) {
        this.listener = listener;
    }

    public static void main(String[] args) throws Exception {
        IMClientImpl client = IMClientImpl.init("127.0.0.1", 9090);
        client.connect("zhangsong");
    }

    @Override
    public void sendMessage(Message message) {
        Protocol proto = new Protocol();
        proto.setOperation(Operation.MSG_SEND.getValue());
        proto.setSequence(new Date().getTime());
        proto.setBody(JsonUtils.toJson(message).getBytes());

        listenerManager.addListener(EventType.RESPONSE, new ActionListener() {

            private Long sequence = proto.getSequence();

            @Override
            public Long getSequence() {
                return sequence;
            }

            @Override
            public void actionPerformed(ActionEvent event) {

            }
        });

        channelFuture.channel().writeAndFlush(proto);
    }
}
