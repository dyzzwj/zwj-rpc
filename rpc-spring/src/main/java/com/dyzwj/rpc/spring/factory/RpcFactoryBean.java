package com.dyzwj.rpc.spring.factory;

import org.springframework.beans.factory.FactoryBean;

public class RpcFactoryBean<T> implements FactoryBean<T> {

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }



}
