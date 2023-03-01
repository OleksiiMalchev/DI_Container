package com.malchev.spring.factory;


import com.malchev.spring.annotation.AutoInjection;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.configurator.BeanConfigurator;
import com.malchev.spring.configurator.JavaBeanConfigurator;
import com.malchev.spring.proxy.BeanProxyTiming;
import com.malchev.spring.scanner.BeanScanner;
import com.malchev.spring.scanner.BeanScannerImpl;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class BeanFactory {
    private static final BeanFactory BEAN_FACTORY = new BeanFactory(); // потокобезопастность?
    private final BeanConfigurator beanConfigurator;
    private final BeanScanner beanScanner;
    private static List<Class<?>> classesInPackage;
    private BeanProxyTiming beanProxyTiming;



    private BeanFactory() {
        this.beanScanner = new BeanScannerImpl();
        this.beanConfigurator = new JavaBeanConfigurator();
        this.classesInPackage = this.beanScanner.findClassInPackage();
    }

    public static BeanFactory getInstance() {
        return BEAN_FACTORY;
    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T bean = null;
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplClass(implementationClass);
        }
        if (implementationClass.getDeclaredConstructors().length >= 1) {
            Optional<Constructor<?>> constructor = Arrays.stream(implementationClass.getDeclaredConstructors())
                    .filter(c -> c.isAnnotationPresent(AutoInjection.class)).findAny();
            if (constructor.isEmpty()) {
                bean = implementationClass.getDeclaredConstructor().newInstance();
                setBeanToField(implementationClass, bean);
                setBeanToMethod(implementationClass, bean);
            } else {
                List<Class<?>> classes = Arrays.stream(constructor.get().getParameterTypes()).toList();
                Object[] objects = checkParamForConstructor(classes);
                bean = (T) constructor.get().newInstance(objects);
                setBeanToField(implementationClass, bean);
                setBeanToMethod(implementationClass, bean);
            }
        }
        return newProxyBean(bean);
    }

    public void createBeans() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Class<?> clazz : classesInPackage) {
            if (!clazz.isAnnotationPresent(Bean.class)) {
                continue;
            }
            BEAN_FACTORY.getBean(clazz);
        }
    }

    private void setBeanToField(Class<?> clazz, Object bean) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(AutoInjection.class)).toList();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(bean, BEAN_FACTORY.getBean(field.getType()));
        }
    }

    private void setBeanToMethod(Class<?> clazz, Object bean) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AutoInjection.class)).toList();
        for (Method method : methods) {
            Class<?> aClass = Arrays.stream(method.getParameterTypes()).findAny().get();
            Map<Class<?>, List<Class<?>>> map = beanConfigurator.getMap();
            Class<?> aClass1 = map.get(aClass).stream().findAny().get();
            Object beanParam = getBean(aClass1);
            method.invoke(bean, beanParam);
        }

    }

    private Object[] checkParamForConstructor(List<Class<?>> params) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] paramsConst = new Object[params.size()];
        int count = 0;
        for (Class<?> clazzParam : params) {
            for (Class<?> clazzPack : classesInPackage) {
                if (clazzParam.equals(clazzPack) && clazzParam.isAnnotationPresent(Bean.class)) {
                    Object bean = getBean(clazzPack);
                    paramsConst[count] = bean;
                    count++;
                }
            }
        }
        return paramsConst;
    }

    private static <T> T newProxyBean (T originBean){
        ClassLoader classLoader = originBean.getClass().getClassLoader();
        Class<?>[] interfaces = originBean.getClass().getInterfaces();
        BeanProxyTiming beanProxyTiming1 = new BeanProxyTiming(originBean);
        Object beanProxy = Proxy.newProxyInstance(classLoader, interfaces, beanProxyTiming1);
        return (T) beanProxy;
    }
}
