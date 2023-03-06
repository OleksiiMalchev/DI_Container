package com.malchev.spring.lottery;

import com.malchev.spring.annotation.AutoInjection;
import com.malchev.spring.annotation.Bean;
import com.malchev.spring.annotation.Lazy;
import com.malchev.spring.service.CashBoxService;
import com.malchev.spring.service.CheckTicketService;
import com.malchev.spring.service.LotteryMachineService;

import java.util.Arrays;
import java.util.Scanner;

@Bean
public class PlayTheLottery implements PlayLottery  {

    @AutoInjection
    @Lazy
    private  CashBoxService cashBoxService;

    private LotteryMachineService lotteryMachineService;

    private final CheckTicketService checkTicketService;
    private static final Scanner scanner = new Scanner(System.in);
    private final int[] userTicket = new int[6];

    @AutoInjection
    public PlayTheLottery(CheckTicketService checkTicketService) {
        this.checkTicketService = checkTicketService;
    }


    public String play() {
        int count = 0;
        int[] winNumbers = lotteryMachineService.selectTicket();
        System.out.println("Pay for the ticket. The cost of one ticket is 20 UAH." +
                "(It is possible to buy one ticket for one draw) Enter the amount:");
        Integer amount = scanner.nextInt();
        if (cashBoxService.buyTicket(amount)) {
            while (count < 6) {
                System.out.println("Enter the number from 0 to 36:");
                int num = scanner.nextInt();
                if (Arrays.stream(userTicket).anyMatch(n -> n == num) || (num <= 0 || num > 36)) {
                    System.out.println("This number has already been entered or unacceptable. " +
                            "Enter the number from 0 to 36:");
                } else {
                    userTicket[count] = num;
                    count++;
                }
            }
            return "Yours numbers:" + Arrays.toString(userTicket) + " Win numbers:" + Arrays.toString(winNumbers) +
                    checkTicketService.checkTicket(userTicket, winNumbers);
        }
        return "Not enough money to buy a ticket";
    }

    public LotteryMachineService getLotteryMachineService() {
        return lotteryMachineService;
    }
    @AutoInjection
    public void setLotteryMachineService(LotteryMachineService lotteryMachineService) {
        this.lotteryMachineService = lotteryMachineService;
    }
}
