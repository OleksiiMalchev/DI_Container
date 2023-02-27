package com.malchev.spring.service.Impl;

import com.malchev.spring.annotation.Bean;
import com.malchev.spring.service.CheckTicketService;
@Bean
public class CheckTicketServiceImpl implements CheckTicketService {

    private Integer numberOfCoincidences = 0;

    @Override
    public String checkTicket(int[] userTicket, int[] lotteryMachineNumbers) {
        for (int i = 0; i < userTicket.length; i++) {
            for (int j = 0; j< userTicket.length;j++){
                if(userTicket[i] == lotteryMachineNumbers[j]){
                    numberOfCoincidences++;
                }
            }
        }
        return "You matched numbers " + numberOfCoincidences + " out of 6";
    }
}
