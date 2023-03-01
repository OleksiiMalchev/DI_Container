package com.malchev.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanProxyTiming implements InvocationHandler {
    private final Map<String, Method> methods = new HashMap<>();
    private Object target;

    public BeanProxyTiming(Object target) {
        this.target = target;
        for (Method method : target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        Object result = methods.get(method.getName()).invoke(target, args);
        long elapsed = System.nanoTime() - start;
        System.out.println("Method " + method.getName() + " completed. Works time is: " + elapsed + " nanosecond");
        return result;
    }
}
