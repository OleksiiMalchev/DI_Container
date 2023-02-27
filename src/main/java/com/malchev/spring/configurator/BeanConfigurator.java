package com.malchev.spring.configurator;

public interface BeanConfigurator {
    <T> Class<? extends T> getImplClass(Class<T> interfaceClass);
}
