package com.dyzwj.rpc.spring.invocation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dyzwj.common.core.Request;
import com.dyzwj.common.core.Response;
import com.dyzwj.rpc.spring.client.Client;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomInvocationHandler implements InvocationHandler {

    private AtomicInteger requestId = new AtomicInteger(0);

    private Client client;

    public CustomInvocationHandler(Client client){
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setRequestId(requestId.addAndGet(1));
        request.setClassName(proxy.getClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        Class<?> returnType = method.getReturnType();
        //发送请求
        Response response = client.send(request);
        //请求失败
        if(response.getCode() != 0){
            throw new Exception(response.getInfo());
        }

        if(returnType.isPrimitive() || returnType.isAssignableFrom(String.class)){
            return response.getData();
        }else if(Collection.class.isAssignableFrom(returnType)){
            return JSONArray.parseArray(response.getData().toString(),Object.class);
        }else if(Map.class.isAssignableFrom(returnType)){
            return JSONObject.parseObject(response.getData().toString(),Map.class);
        }else {
            return JSONObject.parseObject(response.getData().toString(),returnType.getClass());
        }
    }
}
