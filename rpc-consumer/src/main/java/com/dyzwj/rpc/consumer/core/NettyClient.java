package com.dyzwj.rpc.consumer.core;

import com.dyzwj.common.core.Request;
import com.dyzwj.common.core.Response;
import com.dyzwj.rpc.consumer.handler.ClientHandler;
import com.dyzwj.rpc.spring.client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClient implements ApplicationListener<ContextRefreshedEvent>, Client {

    private NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

    @Value("${rpc.server.address}")
    private String serverAddress;

    @Autowired
    private ClientHandler clientHandler;

    private Channel channel;


    /**
     * 重连频率，单位：秒
     */
    private static final Integer RECONNECT_SECONDS = 20;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动netty客户端
        start();
    }

    private void start() {
        new Thread(() -> {
            try{

                String[] split = serverAddress.split(":");
                if(split.length < 2){
                    log.info("rpc服务器地址配置错误");
                    return;
                }
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(split[0],Integer.parseInt(split[1]))
                        .handler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                nioSocketChannel.pipeline().addLast(new StringEncoder(),clientHandler);
                            }
                        });

                log.info("......client init......");
                // 链接服务器，并异步等待成功，即启动客户端
                bootstrap.connect().addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        // 连接失败
                        if (!future.isSuccess()) {
                            log.error("[start][Netty Client 连接服务器({}:{}) 失败]", split[0], split[1]);
                            reconnect();
                            return;
                        }
                        // 连接成功
                        channel = future.channel();
                        log.info("[start][Netty Client 连接服务器({}:{}) 成功]", split[0], split[1]);
                    }

                });
//                ChannelFuture future = bootstrap.connect(split[0], Integer.parseInt(split[1])).sync();
//                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
                log.error("netty client启动出错",e);
            }

        });

    }

    public void reconnect() {
        eventExecutors.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("[reconnect][开始重连]");
                try {
                    start();
                } catch (Exception e) {
                    log.error("[reconnect][重连失败]", e);
                }
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);
        log.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }

    @Override
    public Response send(Request request) {

        return null;
    }
}
