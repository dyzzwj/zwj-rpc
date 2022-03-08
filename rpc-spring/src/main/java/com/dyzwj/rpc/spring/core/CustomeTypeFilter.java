package com.dyzwj.rpc.spring.core;

import com.dyzwj.rpc.spring.anno.Client;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;


public class CustomeTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
//        return true;
        return metadataReader.getAnnotationMetadata().hasAnnotation(Client.class.getName());
    }
}
