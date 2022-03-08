package com.dyzwj.rpc.spring.registry;

public class Instance {

    private String appName;
    private String host;
    private Integer port;

    public Instance() {
    }

    public Instance(String appName, String host, Integer port) {
        this.appName = appName;
        this.host = host;
        this.port = port;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
