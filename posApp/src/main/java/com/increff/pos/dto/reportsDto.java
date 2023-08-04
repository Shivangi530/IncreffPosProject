package com.increff.pos.dto;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.DayOnDaySalesReportData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.DayOnDaySalesPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class reportsDto {
    @Autowired
    private OrderItemService service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DayOnDaySalesService dailySalesService;
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    //      INVENTORY REPORT
    public List<InventoryReportData> getAllInventory() throws ApiException{
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
        for (InventoryPojo p : list) {
            String barcode=productService.selectBarcode(p.getId());
            int brandId=productService.get(p.getId()).getBrand_category();
            String brand= brandService.getCheck(brandId).getBrand();
            String category= brandService.getCheck(brandId).getCategory();
            list2.add(convert(p,barcode,brand,category));
        }
        return list2;
    }

    //      SALES REPORT

    public List<SalesReportData> getReleventAll(LocalDate startDate1,LocalDate endDate1){
        List<SalesReportData> list = new ArrayList<SalesReportData>();
        LocalDateTime startDate = startDate1.atStartOfDay();
        LocalDateTime endDate = endDate1.atTime(LocalTime.MAX);
        List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
        List<OrderItemPojo> orderList= service.getRelevantAll(list1);
        for (OrderItemPojo p : orderList) {
            SalesReportData s= convert(p);
            int i=findBrandCategory(list,s);
            if(i==-1) {
                list.add(convert(p));
            }
            else{
                list.set(i,convert3(list.get(i),p));
            }
        }
        return list;
    }

    public List<SalesReportData> add(@RequestBody SalesReportData form) throws ApiException {
        LocalDate startDate;
        LocalDate endDate;
        if(form.getStartDate()==null){
            startDate= LocalDate.now().minusMonths(1);
        }else{
            startDate= form.getStartDate();
        }
        if(form.getEndDate()==null){
            endDate=LocalDate.now();
        }else{
            endDate= form.getEndDate();
        }

        List<SalesReportData> l=getReleventAll(startDate,endDate);
        return l;
    }

    int findBrandCategory(List<SalesReportData> list,SalesReportData s){
        for(SalesReportData e: list){
            if(e.getBrand().equals(s.getBrand()) && e.getCategory().equals(s.getCategory())){
                return list.indexOf(e);
            }
        }return -1;
    }

    //      DAILY SALES REPORT
    public List<DayOnDaySalesReportData> getAllDailySales() {
        List<DayOnDaySalesReportData> list2 = new ArrayList<DayOnDaySalesReportData>();
        List<DayOnDaySalesPojo> list = dailySalesService.getAll();
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

    private InventoryReportData convert(InventoryPojo p,String barcode,String brand,String category) throws ApiException{
        InventoryReportData d = new InventoryReportData();
        d.setBarcode(barcode);
        d.setQuantity(p.getQuantity());
        d.setId(p.getId());
        d.setBrand(brand);
        d.setCategory(category);
        return d;
    }

    private SalesReportData convert3(SalesReportData d,OrderItemPojo p) {
        d.setQuantity(d.getQuantity()+p.getQuantity());
        d.setRevenue(d.getRevenue()+(p.getSellingPrice()*p.getQuantity()));
        return d;
    }

    private SalesReportData convert(OrderItemPojo p) {
        SalesReportData d = new SalesReportData();
        int brand_category=productDao.select(p.getProductId()).getBrand_category();
        d.setId(p.getId());
        d.setDate((orderDao.select(p.getOrderId()).getDateTime()).toLocalDate());
        d.setBrand(brandDao.select(brand_category).getBrand());
        d.setCategory(brandDao.select(brand_category).getCategory());
        d.setQuantity(p.getQuantity());
        d.setRevenue(p.getSellingPrice()*p.getQuantity());
        return d;
    }
}

//    public List<SalesReportData> getAll() {
//        List<OrderItemPojo> orderList = service.getAll();
//        List<SalesReportData> list = new ArrayList<SalesReportData>();
//        for (OrderItemPojo p : orderList) {
//            SalesReportData s= convert(p);
//            int i=findBrandCategory(list,s);
//            if(i==-1) {
//                list.add(convert(p));
//            }
//            else{
//                list.set(i,convert3(list.get(i),p));
//            }
//        }
//        return list;
//    }