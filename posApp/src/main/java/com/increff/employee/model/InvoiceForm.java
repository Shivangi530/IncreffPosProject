package com.increff.employee.model;

import com.increff.employee.pojo.OrderItemPojo;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    private Integer orderId;
    private String placedDate;
    private List<OrderItem> orderItemList;
    private Double amount;
}
