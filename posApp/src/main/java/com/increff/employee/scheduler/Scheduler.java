package com.increff.employee.scheduler;

import com.increff.employee.model.EmployeeForm;
import com.increff.employee.pojo.EmployeePojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.DayOnDaySalesService;
import com.increff.employee.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;


@Component
@EnableScheduling
public class Scheduler {
    @Autowired
    private DayOnDaySalesService service;

    @Scheduled(cron = "9 0 0 * * ?") // Runs every day at 9AM//    @Scheduled(fixedDelay = 5000)
    public void scheduledTask() throws ApiException {
        // Task logic goes here
        LocalDateTime l = LocalDateTime.now();
        System.out.println(l);
        service.add();
        System.out.println("Scheduler executed");
    }
}
