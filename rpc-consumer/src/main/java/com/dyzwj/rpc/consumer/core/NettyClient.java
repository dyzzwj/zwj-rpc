package com.dyzwj.rpc.consumer.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyClient implements ApplicationListener<ContextRefreshedEvent> {

    private NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

    @Value("${rpc.server.address}")
    private String serverAddress;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动netty客户端
        start();
    }

    private void start() {
        new Thread(() -> {
            try{
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                nioSocketChannel.pipeline().addLast(new StringEncoder());
                            }
                        });
                String[] split = serverAddress.split(":");
                if(split.length < 2){
                    log.info("rpc服务器地址配置错误");
                    return;
                }
                log.info("......client init......");
                ChannelFuture future = bootstrap.connect(split[0], Integer.parseInt(split[1])).sync();
                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
                log.error("netty client启动出错",e);
            }finally {
                eventExecutors.shutdownGracefully();
            }

        });

    }
}
