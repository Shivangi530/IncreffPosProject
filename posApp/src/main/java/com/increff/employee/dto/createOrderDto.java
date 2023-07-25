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
        if (f == null) {
            throw new ApiException("Cannot place empty order.");
        }
        int productId=productService.getIdByBarcode(f.getBarcode());
        int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
        double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
    }
    public void checkAll(List<CreateOrderForm> list) throws ApiException {
        for(CreateOrderForm f:list){
            if (f == null) {
                throw new ApiException("Cannot place empty order.");
            }
            int productId=productService.getIdByBarcode(f.getBarcode());
            int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
            double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
        }
    }

    public void update(CreateOrderForm f) throws ApiException {
        int productId=productService.getIdByBarcode(f.getBarcode());
        int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
        double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
    }

}
