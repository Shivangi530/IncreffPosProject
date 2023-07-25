package com.increff.employee.controller;

import com.increff.employee.dto.reportsDto;
import com.increff.employee.model.DayOnDaySalesReportData;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Api
@RestController
public class ReportsApiController {

    @Autowired
    private reportsDto dto;

    // INVENTORY REPORT
    @ApiOperation(value = "Get list of inventory")
    @RequestMapping(path = "/api/inventoryReport", method = RequestMethod.GET)
    public List<InventoryReportData> getAllInventory() throws ApiException{
        return dto.getAllInventory();
    }



    // SALES REPORT
    @ApiOperation(value = "Get list of all orders")
    @RequestMapping(path = "/api/report/salesReport/relevent", method = RequestMethod.GET)
    public List<SalesReportData> getReleventAll(LocalDate startDate1,LocalDate endDate1){//,String brand,String category) {
        return dto.getReleventAll(startDate1,endDate1);
    }

    @ApiOperation(value = "Get list of all orders")
    @RequestMapping(path = "/api/report/salesReport", method = RequestMethod.GET)
    public List<SalesReportData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Show date time")
    @RequestMapping(path = "/api/report/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> add(@RequestBody SalesReportData form) throws ApiException {
        return dto.add(form);
    }

//          DAILY SALES REPORT
    @ApiOperation(value = "Get list of sales")
    @RequestMapping(path = "/api/report/invoiceReport", method = RequestMethod.GET)
    public List<DayOnDaySalesReportData> getAllDailySales() {
        return dto.getAllDailySales();
    }
}



//todo: create drop down and make it searchable
