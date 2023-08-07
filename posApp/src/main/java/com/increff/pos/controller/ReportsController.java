package com.increff.pos.controller;

import com.increff.pos.dto.reportsDto;
import com.increff.pos.model.DayOnDaySalesReportData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportsController {

    @Autowired
    private reportsDto dto;

    // INVENTORY REPORT
    @ApiOperation(value = "Get list of inventory")
    @RequestMapping(path = "/api/inventoryReport", method = RequestMethod.GET)
    public List<InventoryReportData> getAllInventory() throws ApiException{
        return dto.getAllInventory();
    }

    // SALES REPORT
    @ApiOperation(value = "Show sales by date time")
    @RequestMapping(path = "/api/report/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> add(@RequestBody SalesReportData form) throws ApiException {
        return dto.add(form);
    }

    //DAILY SALES REPORT
    @ApiOperation(value = "Get list of sales")
    @RequestMapping(path = "/api/report/invoiceReport", method = RequestMethod.GET)
    public List<DayOnDaySalesReportData> getAllDailySales() {
        return dto.getAllDailySales();
    }
}
