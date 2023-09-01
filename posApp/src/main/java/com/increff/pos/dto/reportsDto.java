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
import com.increff.pos.pojo.OutwardOrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

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
    public List<InventoryReportData> getAllInventory() throws ApiException {
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
        for (InventoryPojo p : list) {
            String barcode = productService.selectBarcode(p.getId());
            int brandId = productService.get(p.getId()).getBrandCategory();
            String brand = brandService.getCheck(brandId).getBrand();
            String category = brandService.getCheck(brandId).getCategory();
            list2.add(convert(p, barcode, brand, category));
        }
        return list2;
    }

    //      SALES REPORT
    public List<SalesReportData> add(SalesReportData form) throws ApiException {
        LocalDate startDate;
        LocalDate endDate;
        if (form.getStartDate() == null) {
            startDate = LocalDate.now().minusMonths(1);
        } else {
            startDate = form.getStartDate();
        }
        if (form.getEndDate() == null) {
            endDate = LocalDate.now();
        } else {
            endDate = form.getEndDate();
        }

        List<SalesReportData> list = new ArrayList<SalesReportData>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<OutwardOrderPojo> list1 = orderService.getOrderDates(startDateTime, endDateTime);
        List<OrderItemPojo> orderList = service.getRelevantAll(list1);
        for (OrderItemPojo p : orderList) {
            int brandCategory = productDao.select(p.getProductId()).getBrandCategory();
            LocalDate date = orderDao.select(p.getOrderId()).getDateTime().toLocalDate();
            String brand = brandDao.select(brandCategory).getBrand();
            String category = brandDao.select(brandCategory).getCategory();
            SalesReportData s = convert(p, date, brand, category);
            int i = findBrandCategory(list, s);
            if (i == -1) {
                list.add(s);
            } else {
                list.set(i, convert(list.get(i), p));
            }
        }
        return list;
    }

    int findBrandCategory(List<SalesReportData> list, SalesReportData s) {
        for (SalesReportData e : list) {
            if (e.getBrand().equals(s.getBrand()) && e.getCategory().equals(s.getCategory())) {
                return list.indexOf(e);
            }
        }
        return -1;
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
}
