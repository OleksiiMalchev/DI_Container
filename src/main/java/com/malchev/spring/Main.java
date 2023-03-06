package com.malchev.spring;

import com.malchev.spring.factory.BeanFactory;
import com.malchev.spring.lottery.PlayLottery;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanFactory instance = BeanFactory.getInstance();
        instance.createBeans();
        PlayLottery bean = instance.getBean(PlayLottery.class);
        String play = bean.play();
        System.out.println(play);

    }
}