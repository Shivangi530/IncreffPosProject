package com.increff.pos.scheduler;

import com.increff.pos.service.ApiException;
import com.increff.pos.service.DayOnDaySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Component
@EnableScheduling
public class Scheduler {
    @Autowired
    private DayOnDaySalesService service;

    @Scheduled(fixedDelay = 3600000)
    public void scheduledTask() throws ApiException {
        ZonedDateTime l = ZonedDateTime.now();
        System.out.println(l);
        service.add();
        System.out.println("Scheduler executed");
    }
}
