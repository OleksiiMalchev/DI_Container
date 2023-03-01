package com.malchev.spring.configurator;

import java.util.List;
import java.util.Map;

public interface BeanConfigurator {
    <T> Class<? extends T> getImplClass(Class<T> interfaceClass);
    Map<Class<?>, List<Class<?>>> getMap();
}
