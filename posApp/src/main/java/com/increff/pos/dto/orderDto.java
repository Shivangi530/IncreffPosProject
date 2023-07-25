package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Component
public class orderDto {
    @Autowired
    private OrderService service;

    public int add() throws ApiException {
        OrderPojo p = new OrderPojo();
        service.add(p);
        return p.getId();
    }

    public OrderData get(int id) throws ApiException {
        OrderPojo p = service.get(id);
        return convert(p);
    }
    public List<OrderData> getAll() {
        List<OrderPojo> list = service.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OrderPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(@PathVariable int id) throws ApiException {
        service.update(id);
    }

    private static OrderData convert(OrderPojo p) {
        OrderData d = new OrderData();
        d.setId(p.getId());
        d.setStatus(p.getStatus());
        d.setDateTime(p.getDateTime());
        return d;
    }

    private static OrderPojo convert(OrderForm f) {
        OrderPojo p = new OrderPojo();
        p.setStatus(true);
        return p;
    }

    public ResponseEntity<byte[]> getInvoicePDF( Integer id) throws Exception{
        return service.getInvoicePDF(id);
    }
}
