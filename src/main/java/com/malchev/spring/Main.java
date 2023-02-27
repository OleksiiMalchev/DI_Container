package com.malchev.spring;

import com.malchev.spring.factory.BeanFactory;
import com.malchev.spring.lottery.PlayTheLottery;


public class Main {

    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();
        PlayTheLottery play = new PlayTheLottery();
        String result = play.play();
        System.out.println(result);
    }
}