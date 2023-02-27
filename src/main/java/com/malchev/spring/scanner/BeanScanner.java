package com.malchev.spring.scanner;

import java.util.List;

public interface BeanScanner {

    List<Class<?>> findClassInPackage();


}
