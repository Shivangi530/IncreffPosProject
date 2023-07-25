package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    public void add( OrderItemForm form) throws ApiException {
        int productId=productService.getIdByBarcode(form.getBarcode());
        OrderItemPojo p = convert(form,productId);
        InventoryPojo i=inventoryService.get(p.getProductId());
        i.setQuantity(i.getQuantity()-p.getQuantity());
        inventoryService.update(i.getId(),i);
        service.add(p);
    }

    public void delete( int id,OrderItemForm f) throws ApiException {
        OrderItemPojo p = service.get(id);
        InventoryPojo i=inventoryService.get(p.getProductId());
        i.setQuantity(i.getQuantity()-f.getInventoryQty());
        inventoryService.update(i.getId(),i);
        service.delete(id);
    }

    @ApiOperation(value = "Get an orderItem by ID")
    @RequestMapping(path = "/api/orderItem/{id}", method = RequestMethod.GET)
    public OrderItemData get( int id) throws ApiException {
        OrderItemPojo p = service.get(id);
        String barcode= productService.get(p.getProductId()).getBarcode();
        return convert(p,barcode);
    }
    @ApiOperation(value = "Get the list of orderItem by orderID")
    @RequestMapping(path = "/api/orderItem/view/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderList( int id) throws ApiException {
        List<OrderItemPojo> list = service.getOrderList(id);;
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo p : list) {
            String barcode=productService.get(p.getProductId()).getBarcode();
            String productName=productService.get(p.getProductId()).getName();
            list2.add(convert2(p,productName,barcode));
        }
        return list2;
    }

    @ApiOperation(value = "Get list of all orderItems")
    @RequestMapping(path = "/api/orderItem", method = RequestMethod.GET)
    public List<OrderItemData> getAll() throws ApiException{
        List<OrderItemPojo> list = service.getAll();
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo p : list) {
            String barcode= productService.get(p.getProductId()).getBarcode();
            list2.add(convert(p,barcode));
        }
        return list2;
    }

    @ApiOperation(value = "Updates an orderItem")
    @RequestMapping(path = "/api/orderItem/{id}", method = RequestMethod.PUT)
    public void update( int id,  OrderItemForm f) throws ApiException {
        OrderItemPojo p = service.get(id);
        boolean status= orderService.get(p.getOrderId()).getStatus();
        if(status==false) {
            OrderItemPojo newp = convert3(f, p);
            InventoryPojo i = inventoryService.get(p.getProductId());
            if(f.getInventoryQty()>0) {
                inventoryService.checkQuantity(f.getInventoryQty(), i.getId());
            }
            i.setQuantity(i.getQuantity() - f.getInventoryQty());
            inventoryService.update(i.getId(), i);
            service.update(id, newp);
        }else{
            throw new ApiException("Invalid Operation: Invoice has been generated for the order. ");
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
    private OrderItemPojo convert3(OrderItemForm f,OrderItemPojo p){
        p.setQuantity(f.getQuantity());
        p.setSellingPrice(f.getSellingPrice());
        return p;
    }
}
