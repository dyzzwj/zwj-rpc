package com.dyzwj.rpc.consumer.handler;

import com.alibaba.fastjson.JSONObject;
import com.dyzwj.common.core.Request;
import com.dyzwj.common.core.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.SynchronousQueue;

/**
 *  入栈
 */
@Component
public class ClientHandler extends ChannelInboundHandlerAdapter {


    private ConcurrentMap<Integer, SynchronousQueue<String>> requestMap = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    public Response sendRequest(Channel channel, Request request) {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        requestMap.put(request.getRequestId(),queue);
        channel.writeAndFlush(request);
        String take = null;
        try {
            take = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Response.errorResponse();
        }
        return JSONObject.parseObject(take,Response.class);
    }
}
