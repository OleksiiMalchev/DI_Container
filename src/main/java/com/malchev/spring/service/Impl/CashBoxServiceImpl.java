package com.malchev.spring.service.Impl;

import com.malchev.spring.annotation.Bean;
import com.malchev.spring.service.CashBoxService;
@Bean
public class CashBoxServiceImpl implements CashBoxService {
    private final Integer ticketPrice = 20;

    @Override
    public boolean buyTicket(Integer amount) {
        return amount >= ticketPrice;
    }
}
