package com.malchev.spring.service.Impl;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.service.LotteryMachineService;

@Bean
public class LotteryMachineServiceImpl implements LotteryMachineService {

    @Override
    public int[] selectTicket() {
        int[] ticketNumbers = new int[6];
        for (int i = 0; i < ticketNumbers.length; i++) {
            ticketNumbers[i] = (int) (Math.random()*36+1);
        }
        return ticketNumbers;
    }
}
