package com.increff.pos.scheduler;

import com.increff.pos.service.ApiException;
import com.increff.pos.service.DayOnDaySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@EnableScheduling
public class Scheduler {
    @Autowired
    private DayOnDaySalesService service;

    @Scheduled(cron = "0 2 11 * * ?") // Runs every day at 9AM//    @Scheduled(fixedDelay = 5000)
    public void scheduledTask() throws ApiException {
        LocalDateTime l = LocalDateTime.now();
        System.out.println(l);
        service.add();
        System.out.println("Scheduler executed");
    }
}
