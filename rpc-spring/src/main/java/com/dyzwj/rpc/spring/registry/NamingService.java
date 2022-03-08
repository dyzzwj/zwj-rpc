package com.dyzwj.rpc.spring.registry;

import java.util.List;

public interface NamingService {

    /**
     * 注册
     * @param instance
     */
    void register(Instance instance);


    /**
     * 下线
     * @param instance
     */
    void deregister(Instance instance);

    /**
     * 查询所有实例
     * @return
     */
    List<Instance> getAllInstance();

    /**
     * 查询指定实例
     * @return
     */
    List<Instance> selectInstance(String appName);

}
