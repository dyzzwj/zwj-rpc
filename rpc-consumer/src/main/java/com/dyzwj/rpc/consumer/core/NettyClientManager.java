package com.dyzwj.rpc.consumer.core;

import com.dyzwj.rpc.spring.client.Client;

import java.util.HashMap;
import java.util.Map;

public class NettyClientManager {

    private Map<String,NettyClient> clientMap = new HashMap<>();

    public Client chooseClient(String appName) throws Exception {
        NettyClient nettyClient = clientMap.get(appName);
        if(nettyClient == null){
            synchronized (clientMap){
                nettyClient = clientMap.get(appName);
                if(nettyClient == null){
                    throw new Exception("非法的应用");
                }
            }
        }
        return nettyClient;
    }



}
