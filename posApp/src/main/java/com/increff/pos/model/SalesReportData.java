package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalesReportData {
//todo: set not null conditions for startDate and endDate
    private int id;
    private LocalDate date;
    private LocalDate startDate;
    private LocalDate endDate;
    private String brand;
    private String category;
    private int quantity;
    private double revenue;

}
