package com.increff.employee.dto;
import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.DayOnDaySalesReportData;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.DayOnDaySalesPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.*;
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
            String brand= brandService.get(brandId).getBrand();
            String category= brandService.get(brandId).getCategory();
            list2.add(convert(p,barcode,brand,category));
        }
        return list2;
    }

    //      SALES REPORT
    int findBrandCategory(List<SalesReportData> list,SalesReportData s){
        for(SalesReportData e: list){
            if(e.getBrand().equals(s.getBrand()) && e.getCategory().equals(s.getCategory())){
                System.out.println("found "+ e.getId());
                return list.indexOf(e);
            }
        }return -1;
    }

    public List<SalesReportData> getReleventAll(LocalDate startDate1,LocalDate endDate1){//,String brand,String category) {
        System.out.println("GetRelevent in api!!!!!!!!!!!!!!!!!!!");
        List<SalesReportData> list = new ArrayList<SalesReportData>();
        LocalDateTime startDate = startDate1.atStartOfDay();
        LocalDateTime endDate = endDate1.atTime(LocalTime.MAX);
        List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
        for(OrderPojo op: list1 ){
            System.out.println("orderpojo: "+op.getId());
        }
        List<OrderItemPojo> orderList= service.getRelevantAll(list1);
        for (OrderItemPojo p : orderList) {
            System.out.println("order Item pojo: "+p.getId()+" orderpojo: "+p.getOrderId());
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

    public List<SalesReportData> getAll() {
        List<OrderItemPojo> orderList = service.getAll();
        List<SalesReportData> list = new ArrayList<SalesReportData>();
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
        LocalDate startDate= form.getStartDate();
        LocalDate endDate= form.getEndDate();
        String brand= form.getBrand();
        String category= form.getCategory();
        System.out.println("startDate= "+startDate+" + endDate= " + endDate);
        List<SalesReportData> l=getReleventAll(startDate,endDate);//,brand,category);
        return l;
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
    private SalesReportData convert3(SalesReportData d,OrderItemPojo p) {
        d.setQuantity(d.getQuantity()+p.getQuantity());
        d.setRevenue(d.getRevenue()+(p.getSellingPrice()*p.getQuantity()));
        return d;
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
}
