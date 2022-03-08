package com.dyzwj.rpc.spring.anno;


import com.dyzwj.rpc.spring.core.ClientRegistra;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientRegistra.class)
public @interface EnableClients {

    String value() default "";

    String basePackage();
}
