package com.dyzwj.rpc.producer.handler;


import com.alibaba.fastjson.JSONObject;
import com.dyzwj.common.core.Request;
import com.dyzwj.common.core.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String,Object> serviceMap = new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String req = (String) msg;
        Request request = JSONObject.parseObject(req,Request.class);
        log.info("RPC客户端请求接口:" + request.getClassName()+"   方法名:" + request.getMethodName());
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        try{
            Object res = handle(request);
            response.setData(res);

        }catch (Exception e){
            response.setInfo(e.toString());
            response.setCode(1);
            log.error("RPC Server handle request error",e);
        }

        ctx.writeAndFlush(response);

    }

    private Object handle(Request request) throws Exception {
        String className = request.getClassName();
        Object bean = serviceMap.get(className);
        if(bean == null){
            synchronized (serviceMap){
                bean = serviceMap.get(className);
                if(bean == null){
                    bean = applicationContext.getBean(Class.forName(className));
                    if(bean != null){
                        serviceMap.put(className,bean);
                    }else{
                        throw new Exception("未找到服务接口,请检查配置!:"+className+"#"+request.getMethodName());
                    }
                }
            }
        }
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Class<?> aClass = bean.getClass();
        Method method = aClass.getMethod(methodName, parameterTypes);
        //执行方法
        return method.invoke(bean,getParameters(parameterTypes,request.getParameters()));
    }

    private Object[] getParameters(Class<?>[] parameterTypes, Object[] parameters) {
        if(parameters == null || parameters.length == 0){
            return parameters;
        }else{
            Object[] param = new Object[parameters.length];
            for (int i = 0; i < param.length; i++) {
                param[i] = JSONObject.parseObject(parameters[i].toString(),parameterTypes[i]);
            }
            return param;
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        log.info("客户端连接成功!"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        log.info("客户端断开连接!{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)   {
        log.info(cause.getMessage());
        ctx.close();
    }


}
