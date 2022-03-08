package com.dyzwj.rpc.spring.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class ClassPathScanner extends ClassPathScanningCandidateComponentProvider {

    public ClassPathScanner(){}

    public ClassPathScanner(boolean useDefaultFilters){
        super(useDefaultFilters);
    }

    /**
     *  原方法会判断是不是具体的类（不能是接口）
     *   protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
     * 		AnnotationMetadata metadata = beanDefinition.getMetadata();
     * 		return (metadata.isIndependent() && (metadata.isConcrete() ||
     * 				(metadata.isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))));
     *    }
      * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }


}
