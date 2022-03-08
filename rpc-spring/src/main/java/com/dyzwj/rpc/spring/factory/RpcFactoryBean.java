package com.dyzwj.rpc.spring.factory;

import com.dyzwj.rpc.spring.invocation.CustomInvocationHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

public class RpcFactoryBean<T> implements FactoryBean<T> {

    @Autowired
    private CustomInvocationHandler invocationHandler;

    //模仿mybatis
    private Class<?> clazz;

    public RpcFactoryBean(){}

    public  RpcFactoryBean(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        return getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    private T getProxy(){
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz},invocationHandler);
    }



}
