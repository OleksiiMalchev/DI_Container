package com.malchev.spring.configurator;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.scanner.BeanScanner;
import com.malchev.spring.scanner.BeanScannerImpl;

import java.util.*;

public class JavaBeanConfigurator implements BeanConfigurator {

    private final BeanScanner beanScanner;
    private Map<Class<?>, List<Class<?>>> interfaceWithImpl= new HashMap<>();
    private List<Class<?>> classesInPackage;



    public JavaBeanConfigurator() {
        this.beanScanner = new BeanScannerImpl();
        this.classesInPackage =  beanScanner.findClassInPackage();
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> interfaceClass) {

        for(Class<?> clazz:classesInPackage){
            if(!clazz.isAnnotationPresent(Bean.class)){
                continue;
            }
            if(clazz.isInterface()){
                List<Class<?>> implClass = new ArrayList<>();
                for(Class<?> iClass:classesInPackage){
                    if(!iClass.isInterface()&& Arrays.asList(iClass.getInterfaces()).contains(clazz)){
                        implClass.add(iClass);
                    }
                }
                interfaceWithImpl.put(clazz,implClass);
            }
        }
        List<Class<?>> classList = interfaceWithImpl.get(interfaceClass);
        if(classList.size()!=1){
            throw new RuntimeException("Interface has 0 or more than 1 impl");
        }
        return (Class<? extends T>) classList.stream().findFirst().get();
    }

    @Override
    public Map<Class<?>, List<Class<?>>> getMap() {
        return interfaceWithImpl;
    }


}


