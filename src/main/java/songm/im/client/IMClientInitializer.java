/*
 * Copyright (c) 2016, zhangsong <songm.cn>.
 *
 */

package songm.im.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import songm.im.client.event.ActionListenerManager;

/**
 * Tcp管道初始化
 *
 * @author zhangsong
 * @since 0.1, 2016-8-9
 * @version 0.1
 * 
 */
public class IMClientInitializer extends ChannelInitializer<SocketChannel> {

    private ProtocolCodec protocolCodec;
    private IMClientHandler clientHandler;

    public IMClientInitializer(ActionListenerManager listenerManager) {
        protocolCodec = new ProtocolCodec();
        clientHandler = new IMClientHandler(listenerManager);
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(protocolCodec);
        ch.pipeline().addLast(clientHandler);
    }

}
