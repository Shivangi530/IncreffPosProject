package com.increff.employee.controller;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.model.DayOnDaySalesReportData;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.DayOnDaySalesPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class DayOnDaySalesReportApiController {

    @Autowired
    private DayOnDaySalesService service;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private OrderDao orderDao;

    @ApiOperation(value = "Get list of sales")
    @RequestMapping(path = "/api/report/invoiceReport", method = RequestMethod.GET)
    public List<DayOnDaySalesReportData> getAll() {
        List<DayOnDaySalesReportData> list2 = new ArrayList<DayOnDaySalesReportData>();
        List<DayOnDaySalesPojo> list = service.getAll();
        for (DayOnDaySalesPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }
    private static DayOnDaySalesReportData convert(DayOnDaySalesPojo p) {
        DayOnDaySalesReportData d = new DayOnDaySalesReportData();
        d.setRevenue(p.getRevenue());
        d.setOrderCount(p.getOrderCount());
        d.setDate(p.getDate());
        d.setItemCount(p.getItemCount());
        return d;
    }

}
