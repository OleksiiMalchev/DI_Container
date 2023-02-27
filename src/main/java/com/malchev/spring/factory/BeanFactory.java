package com.malchev.spring.factory;


import com.malchev.spring.annotation.AutoInjection;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.configurator.BeanConfigurator;
import com.malchev.spring.configurator.JavaBeanConfigurator;
import com.malchev.spring.scanner.BeanScanner;
import com.malchev.spring.scanner.BeanScannerImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


public class BeanFactory {
    private static final BeanFactory BEAN_FACTORY = new BeanFactory();
    private final BeanConfigurator beanConfigurator;
    private final BeanScanner beanScanner;
    private static List<Class<?>> classesInPackage;

    public BeanFactory() {
        this.beanScanner = new BeanScannerImpl();
        this.beanConfigurator = new JavaBeanConfigurator();
        this.classesInPackage =  this.beanScanner.findClassInPackage();
    }

    public static BeanFactory getInstance() {
        return BEAN_FACTORY;
    }

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
    public  void createBeans( ) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Class<?> clazz:classesInPackage){
            if(!clazz.isAnnotationPresent(Bean.class)){
                continue;
            }
            BEAN_FACTORY.getBean(clazz);
        }
    }
}
