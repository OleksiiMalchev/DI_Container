package com.malchev.spring;

import com.malchev.spring.annotation.AutoInjection;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.factory.BeanFactory;
import com.malchev.spring.lottery.PlayLottery;




import java.lang.reflect.InvocationTargetException;

@Bean
public class Main {
    @AutoInjection
    static PlayLottery playTheLottery;


    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanFactory.getInstance().createBeans();
        String play = playTheLottery.play();
        System.out.println(play);


    }
}