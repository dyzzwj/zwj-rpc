package com.dyzwj.rpc.spring.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class LocalFileNamingService implements NamingService {

    @Value("${registry.file.path}")
    private String filePath;


    @Override
    public void register(Instance instance) {
        try{
            File file = new File(filePath + instance.getAppName());
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            StringBuilder sb = new StringBuilder();
            sb.append(instance.getHost());
            sb.append(":");
            sb.append(instance.getPort());
            sb.append("\n");
            fileWriter.write(sb.toString());
        }catch (Exception e){
            log.error("服务注册失败",e.toString());
        }

    }

    @Override
    public void deregister(Instance instance) {

    }

    @Override
    public List<Instance> getAllInstance() {
        return null;
    }

    @Override
    public List<Instance> selectInstance(String appName) {
        try{
            File file = new File(filePath + appName);
            if(!file.exists()){
                return Collections.EMPTY_LIST;
            }



        }catch (Exception e){
            log.error("服务注册失败",e.toString());
        }

        return null;
    }
}
