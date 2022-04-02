package com.dyzwj.rpc.consumer.core;

import com.dyzwj.rpc.spring.client.Connection;
import io.netty.channel.Channel;

public class NettyConnection implements Connection {

    private Channel channel;

    public NettyConnection(Channel channel){
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
