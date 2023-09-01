package com.increff.pos.dto;

import com.increff.pos.model.EnumData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

@Component
public class orderItemDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService service;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    @Transactional
    public void delete( int id) throws ApiException {
        OrderItemPojo pojo = service.getCheck(id);
        EnumData.Status status= orderService.getCheck(pojo.getOrderId()).getStatus();
        if(status== EnumData.Status.INVOICED) {
            throw new ApiException("Invalid Operation: Invoice has been generated for the order. ");
        }
        int inventoryQty=inventoryService.getCheck(pojo.getProductId()).getQuantity();
        inventoryService.update(pojo.getProductId(),inventoryQty+ pojo.getQuantity());
        service.delete(id);
    }

    //Get the list of orderItem by orderID
    @Transactional
    public List<OrderItemData> getOrderList( int id) throws ApiException {
        List<OrderItemPojo> pojoList = service.getOrderList(id);
        List<OrderItemData> dataList = new ArrayList<>();
        for (OrderItemPojo pojo : pojoList) {
            String barcode=productService.getCheck(pojo.getProductId()).getBarcode();
            String productName=productService.getCheck(pojo.getProductId()).getName();
            dataList.add(convert(pojo,productName,barcode));
        }
        return dataList;
    }

    @Transactional
    public void update( int id,  OrderItemForm f) throws ApiException {
        OrderItemPojo pojo = service.getCheck(id);
        if(f.getQuantity()>10000000){
            throw new ApiException("Quantity is too large");
        }
        int newQty= f.getQuantity()- pojo.getQuantity();

        EnumData.Status status= orderService.getCheck(pojo.getOrderId()).getStatus();
        if(status== EnumData.Status.CREATED) {
            // Checking if inventory is sufficient
            int inventoryPrevQty = inventoryService.getCheck(pojo.getProductId()).getQuantity();
            if(inventoryPrevQty< newQty){
                throw new ApiException("Selected quantity:"+f.getQuantity()+" is more than inventory: "+inventoryPrevQty+" for barcode:"+productService.get(pojo.getProductId()).getBarcode());
            }
            int inventoryUpdatedQty = inventoryPrevQty - newQty;

            //Checking if the selling price is less than mrp.
            int productId=pojo.getProductId();
            productService.checkSellingPrice(f.getSellingPrice(),productId);
            double sellingPrice= Math.round(f.getSellingPrice()  * 100.0) / 100.0;

            //Updating the inventory
            if(inventoryUpdatedQty<0) {
                throw new ApiException("Invalid operation");
            }
            inventoryService.update(pojo.getProductId(), inventoryUpdatedQty);
            service.update(id, f.getQuantity(),sellingPrice);
        }else if(status== EnumData.Status.INVOICED){
            throw new ApiException("Invalid Operation: Invoice has been generated for the order. ");
        }else{
            throw new ApiException("Invalid Operation: Empty order. ");
        }
    }

}
