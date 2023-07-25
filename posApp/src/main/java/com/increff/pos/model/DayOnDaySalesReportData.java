package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayOnDaySalesReportData {
    private LocalDate date;
    private int orderCount;
    private int itemCount;
    private double revenue;
}