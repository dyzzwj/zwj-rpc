package com.dyzwj.rpc.spring.core;

import com.dyzwj.rpc.spring.anno.EnableClients;
import com.dyzwj.rpc.spring.factory.RpcFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

public class ClientRegistra implements ImportBeanDefinitionRegistrar {

    ClassPathScanner scanner = new ClassPathScanner(false);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableClients.class.getName());
        String basePackage = (String) attributes.get("basePackage");
        scanner.addIncludeFilter(new CustomeTypeFilter());
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
        candidateComponents.forEach(this::processBeanDefinition);

    }

    private void processBeanDefinition(BeanDefinition beanDefinition){
        GenericBeanDefinition bd = (GenericBeanDefinition) beanDefinition;


        bd.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
        bd.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        //偷天换日
        // 设置接口的 beanClass 都为 RpcFactoryBean<?>，因为 RpcFactoryBean 实现了 FactoryBean 接口，这样初始化 Bean 时就会调用 getObject 方法
        bd.setBeanClass(RpcFactoryBean.class);
    }

}
