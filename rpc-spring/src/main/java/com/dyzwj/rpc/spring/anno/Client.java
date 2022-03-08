package com.dyzwj.rpc.spring.anno;


import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Client {


    String value() default "";
    //应用名
    String name();
}
