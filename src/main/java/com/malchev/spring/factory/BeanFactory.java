package com.malchev.spring.factory;


import com.malchev.spring.annotation.AutoInjection;
import com.malchev.spring.configurator.BeanConfigurator;
import com.malchev.spring.configurator.JavaBeanConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


public class BeanFactory {
    private static final BeanFactory BEAN_FACTORY = new BeanFactory();
    private final BeanConfigurator beanConfigurator;

    private BeanFactory() {
        this.beanConfigurator = new JavaBeanConfigurator();
    }

//    public static BeanFactory getInstance() {
//        return BEAN_FACTORY;
//    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Class<? extends T> implementationClass = clazz;
        if(implementationClass.isInterface()){
            implementationClass = beanConfigurator.getImplClass(implementationClass);
        }
        T bean =  implementationClass.getDeclaredConstructor().newInstance();

        List<Field> fields = Arrays.stream(implementationClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(AutoInjection.class)).toList();

        for(Field field:fields){
            field.setAccessible(true);
            field.set(bean,BEAN_FACTORY.getBean(field.getType()));
        }

        return bean;

    }

}
