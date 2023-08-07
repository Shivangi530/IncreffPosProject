package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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


    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {

        LocalDate date= LocalDate.now();
        List<DayOnDaySalesPojo> salesList= getAll();

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        List<OutwardOrderPojo> orderList = orderService.getOrderDates(startDate,endDate);
        List<OrderItemPojo> orderItemList= service.getRelevantAll(orderList);

        int quantityCount=0;
        double revenue=0.0;
        for (OrderItemPojo p : orderItemList) {
            quantityCount+= p.getQuantity();
            revenue+=( p.getSellingPrice()*p.getQuantity());
        }

        if (!salesList.isEmpty() && salesList.get(salesList.size() - 1).getDate().isEqual(date)) {
            DayOnDaySalesPojo lastElement = salesList.get(salesList.size() - 1);
                lastElement.setItemCount(quantityCount);
                lastElement.setOrderCount(orderList.size());
                lastElement.setRevenue(revenue);
        }else{
            DayOnDaySalesPojo item= new DayOnDaySalesPojo();
            item.setDate(date);
            item.setItemCount(quantityCount);
            item.setOrderCount(orderList.size());
            item.setRevenue(revenue);
            dao.insert(item);
        }
    }

    @Transactional
    public List<DayOnDaySalesPojo> getAll() {
        return dao.selectAll();
    }
}
