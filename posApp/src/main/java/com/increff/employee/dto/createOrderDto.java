package com.increff.employee.dto;

import com.increff.employee.model.CreateOrderData;
import com.increff.employee.model.CreateOrderForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class createOrderDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    public void add(CreateOrderForm f) throws ApiException {
        int productId=productService.getIdByBarcode(f.getBarcode());
        int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
        double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
        // TODO sp<mrp   quantity -ve and 0 and decimal
        // TODO variable names should be readable
//        OrderItemPojo op = convert2(f,productId,quantity,sellingPrice);
    }
    public List<CreateOrderData> getAll() {
        List<CreateOrderData> list2 = new ArrayList<CreateOrderData>();
        return list2;
    }

    public void update(CreateOrderForm f) throws ApiException {
        int productId=productService.getIdByBarcode(f.getBarcode());
        int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
        double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
        // TODO remove convert2
//        OrderItemPojo op = convert2(f,productId,quantity,sellingPrice);
    }

//    private OrderItemPojo convert2(CreateOrderForm f,int productId,int quantity,double sellingPrice) throws ApiException{
//        OrderItemPojo p = new OrderItemPojo();
//        p.setProductId(productId);
//        p.setQuantity(quantity);
//        p.setSellingPrice(sellingPrice);
//        return p;
//    }
}
