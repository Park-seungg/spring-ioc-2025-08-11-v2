package com.ll.framework.ioc;

import com.ll.framework.ioc.annotations.Component;
import com.ll.framework.ioc.annotations.Configuration;
import com.ll.framework.ioc.annotations.Repository;
import com.ll.framework.ioc.annotations.Service;
import com.ll.standard.util.Ut;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Stream;

public class BeanScanner {
    private final String basePackage;
    private final BeanRegistry beanRegistry;

    public BeanScanner(String basePackage, BeanRegistry beanRegistry) {
        this.basePackage = basePackage;
        this.beanRegistry = beanRegistry;
    }

    public void scan() {
        Reflections reflections = new Reflections(basePackage);

        Stream.of(
                reflections.getTypesAnnotatedWith(Component.class),
                reflections.getTypesAnnotatedWith(Service.class),
                reflections.getTypesAnnotatedWith(Repository.class),
                reflections.getTypesAnnotatedWith(Configuration.class)
        )
                .flatMap(Set::stream)
                .filter(clazz -> !clazz.isInterface() && !clazz.isAnnotation() && !clazz.isEnum())
                .forEach(clazz -> {
                    String beanName = Ut.str.lcfirst(clazz.getSimpleName());
                    beanRegistry.registerBeanClass(beanName, clazz);
                });
    }
}