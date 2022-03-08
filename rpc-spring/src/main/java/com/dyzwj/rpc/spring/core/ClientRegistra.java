package com.dyzwj.rpc.spring.core;

import com.dyzwj.rpc.spring.anno.EnableClients;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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


    }
}
