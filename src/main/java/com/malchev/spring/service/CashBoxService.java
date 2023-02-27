package com.malchev.spring.service;

import com.malchev.spring.annotation.Bean;

@Bean
public interface CashBoxService {
    boolean buyTicket(Integer amount);
}
