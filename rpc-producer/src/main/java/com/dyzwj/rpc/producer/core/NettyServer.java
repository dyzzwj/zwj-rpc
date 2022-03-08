package com.dyzwj.rpc.producer.core;


import com.dyzwj.rpc.producer.handler.ServerHandler;
import com.dyzwj.rpc.producer.handler.ServerStartHandler;
import com.dyzwj.rpc.spring.registry.Instance;
import com.dyzwj.rpc.spring.registry.NamingService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NettyServer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ServerHandler serverHandler;

    @Autowired
    private NamingService namingService;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    @Value("${rpc.server.address}")
    private String serverAddress;

    @Value("${spring.application.name}")
    private String applicationName;


    //容器刷新完成
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动netty服务端
        start();
    }

    private void start() {
        new Thread(() -> {
            try{
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new ServerStartHandler())
                        .childHandler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel channel) throws Exception {
                                channel.pipeline().addLast(new StringDecoder(),serverHandler);
                            }
                        });

                String[] split = serverAddress.split(":");
                if(split.length < 2){
                    log.info("rpc服务器地址配置错误");
                    return;
                }
                log.info(".........server  init..........");
                ChannelFuture future = bootstrap.bind(split[0],Integer.parseInt(split[1])).sync();
                log.info(".........server start..........");
                namingService.register(new Instance(applicationName,split[0],Integer.parseInt(split[1])));
                future.channel().closeFuture().sync();

            }catch (Exception e){
                e.printStackTrace();
                log.error("netty server启动出错",e);
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }


}