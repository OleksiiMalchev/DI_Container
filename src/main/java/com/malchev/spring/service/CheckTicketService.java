package com.malchev.spring.service;

import com.malchev.spring.annotation.Bean;

@Bean
public interface CheckTicketService {
    String checkTicket(int[] userTicket, int[] lotteryMachineNumbers);
}
