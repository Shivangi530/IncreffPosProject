package com.increff.pos.dto;

import com.increff.pos.model.EnumData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    public void add( OrderItemForm form) throws ApiException {
        int productId=productService.getIdByBarcode(form.getBarcode());
        OrderItemPojo p = convert(form,productId);
        OrderPojo order = orderService.getCheck(p.getOrderId()); // to check if orderId
        int inventoryQty=inventoryService.getCheck(p.getProductId()).getQuantity();
        inventoryService.update(p.getProductId(),inventoryQty-p.getQuantity());
        service.add(p);
    }

    public void delete( int id) throws ApiException { //todo: Check order status-invoiced before deletion
        OrderItemPojo p = service.getCheck(id);
        EnumData.Status status= orderService.getCheck(p.getOrderId()).getStatus();
        if(status== EnumData.Status.INVOICED) {
            throw new ApiException("Invalid Operation: Invoice has been generated for the order. ");
        }
        int inventoryQty=inventoryService.getCheck(p.getProductId()).getQuantity();
        inventoryService.update(p.getProductId(),inventoryQty+ p.getQuantity());
        service.delete(id);
    }

    @Transactional
    public OrderItemData get( int id) throws ApiException {
        OrderItemPojo p = service.getCheck(id);
        String barcode= productService.getCheck(p.getProductId()).getBarcode();
        return convert(p,barcode);
    }
    @Transactional
    public List<OrderItemData> getOrderList( int id) throws ApiException {
        List<OrderItemPojo> list = service.getOrderList(id);
        List<OrderItemData> list2 = new ArrayList<>();
        for (OrderItemPojo p : list) {
            String barcode=productService.getCheck(p.getProductId()).getBarcode();
            String productName=productService.getCheck(p.getProductId()).getName();
            list2.add(convert2(p,productName,barcode));
        }
        return list2;
    }

    @Transactional
    public List<OrderItemData> getAll() throws ApiException{
        List<OrderItemPojo> list = service.getAll();
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo p : list) {
            String barcode= productService.getCheck(p.getProductId()).getBarcode();
            list2.add(convert(p,barcode));
        }
        return list2;
    }

    @Transactional
    public void update( int id,  OrderItemForm f) throws ApiException {
        OrderItemPojo p = service.getCheck(id);
        int newQty= f.getQuantity()- p.getQuantity() ;
        EnumData.Status status= orderService.getCheck(p.getOrderId()).getStatus();
        if(status== EnumData.Status.CREATED) {
            int inventoryPrevQty = inventoryService.getCheck(p.getProductId()).getQuantity();
            int inventoryUpdatedQty = inventoryPrevQty - newQty;
            if(inventoryUpdatedQty<0) {
                throw new ApiException("Invalid operation");
            }
            inventoryService.update(p.getProductId(), inventoryUpdatedQty);
            service.update(id, f.getQuantity(),f.getSellingPrice());
        }else if(status== EnumData.Status.INVOICED){
            throw new ApiException("Invalid Operation: Invoice has been generated for the order. ");
        }else{
            throw new ApiException("Invalid Operation: Empty order. ");
        }
    }


    private static OrderItemData convert(OrderItemPojo p,String barcode) {
        OrderItemData d = new OrderItemData();
        d.setOrderId(p.getOrderId());
        d.setBarcode(barcode);
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setId(p.getId());
        return d;
    }

    private OrderItemPojo convert(OrderItemForm f,int productId){
        OrderItemPojo p = new OrderItemPojo();
        p.setOrderId(f.getOrderId());
        p.setProductId(productId);
        p.setQuantity(f.getQuantity());
        p.setSellingPrice(f.getSellingPrice());
        return p;
    }
    private OrderItemData convert2(OrderItemPojo p,String productName, String barcode){
        OrderItemData d = new OrderItemData();
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setId(p.getId());
        d.setProductName(productName);
        d.setBarcode(barcode);
        return d;
    }

}
