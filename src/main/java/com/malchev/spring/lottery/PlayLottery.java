package com.malchev.spring.lottery;

import com.malchev.spring.annotation.Bean;
import com.malchev.spring.service.LotteryMachineService;


@Bean
public interface PlayLottery {
    String play();

    public LotteryMachineService getLotteryMachineService();

    void setLotteryMachineService(LotteryMachineService lotteryMachineService);
}
