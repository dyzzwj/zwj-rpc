package com.dyzwj.rpc.spring.client;

public interface ClientManager {

    Client chooseClient(String appName);
}
