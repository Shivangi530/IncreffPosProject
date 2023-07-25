package com.increff.pos.dto;

import com.increff.pos.model.CreateOrderForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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