package com.dyzwj.rpc.consumer;


import com.dyzwj.rpc.spring.anno.EnableClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableClients(basePackage = "com.dyzwj.user.service")
@SpringBootApplication
public class ConsumerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }


}
