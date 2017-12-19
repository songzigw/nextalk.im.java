package cn.songm.im.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.songm.im.codec.Protocol;
import cn.songm.im.codec.Protocol.Operation;
import io.netty.channel.Channel;

public class DisconnectRequestHandler {

    private static Logger log = LoggerFactory.getLogger(DisconnectRequestHandler.class);
    
    public static void buildMessage(Channel ch) {
        Protocol p = new Protocol();
        p.setOperation(Operation.DISCONNECT);
        log.debug("request: {}, {}", p.getOperation(), ch);
        ch.writeAndFlush(p);
    }
}
