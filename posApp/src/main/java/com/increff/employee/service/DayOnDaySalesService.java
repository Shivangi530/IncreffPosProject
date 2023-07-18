package com.increff.employee.service;

import com.increff.employee.dao.*;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DayOnDaySalesService {

    @Autowired
    private OrderItemService service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private DayOnDaySalesDao dao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {
//        System.out.println("DayOnDaySalesService  Add function!!!!!!!!!!!");
        DayOnDaySalesPojo item= new DayOnDaySalesPojo();
        LocalDate date= LocalDate.now().minusDays(1);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
        for(OrderPojo o:list1){
            System.out.println("Order id: "+o.getId()+" Order Status: "+ o.getStatus());
        }
        List<OrderItemPojo> orderList= service.getRelevantAll(list1);

        int quantityCount=0;
        double revenue=0.0;
        for (OrderItemPojo p : orderList) {
            quantityCount+= p.getQuantity();
            revenue+=( p.getSellingPrice()*p.getQuantity());
        }
        item.setDate(date);
        item.setItemCount(quantityCount);
        item.setOrderCount(list1.size());
        item.setRevenue(revenue);
        dao.insert(item);
//        System.out.println("Inserted!!!!!!!!!!!!!!!!!!");
    }

    @Transactional
    public List<DayOnDaySalesPojo> getAll() {
        return dao.selectAll();
    }
}
