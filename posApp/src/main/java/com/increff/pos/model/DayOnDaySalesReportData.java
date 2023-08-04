package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayOnDaySalesReportData {
    private LocalDate date;
    private Integer orderCount;
    private Integer itemCount;
    private double revenue;
}