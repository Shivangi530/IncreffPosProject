package com.increff.pos.dto;

import com.increff.pos.model.CreateOrderForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OutwardOrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

@Component
public class orderDto {
    @Autowired
    private OrderService service;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public int add(List<CreateOrderForm> orderFormList) throws ApiException {
        checkAll(orderFormList);

        OutwardOrderPojo orderPojo = new OutwardOrderPojo();
        service.add(orderPojo);
        int orderId= orderPojo.getId();

        for (CreateOrderForm form : orderFormList) {
            int productId=productService.getIdByBarcode(form.getBarcode());
            int quantity=inventoryService.checkQuantity(form.getQuantity(),productId);
            double sellingPrice=productService.checkSellingPrice(form.getSellingPrice(),productId);

            OrderItemPojo orderItemPojo= new OrderItemPojo();
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setQuantity(quantity);
            orderItemPojo.setProductId(productId);
            orderItemPojo.setSellingPrice(Math.round(sellingPrice  * 100.0) / 100.0);
            int inventoryQty=inventoryService.getCheck(productId).getQuantity();
            inventoryService.update(productId,inventoryQty-quantity);
            orderItemService.add(orderItemPojo);
        }
        return orderId;
    }

    public OrderData get(int id) throws ApiException {
        OutwardOrderPojo orderPojo = service.getCheck(id);
        return convert(orderPojo);
    }
    public List<OrderData> getAll() {
        List<OutwardOrderPojo> pojoList = service.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OutwardOrderPojo orderPojo : pojoList) {
            list2.add(convert(orderPojo));
        }
        return list2;
    }

    public void update(int id,String status) throws ApiException {
        service.update(id,status);
    }

    public void check(CreateOrderForm f) throws ApiException {
        if (f == null) {
            throw new ApiException("Cannot place empty order.");
        }
        int productId=productService.getIdByBarcode(f.getBarcode());
        int quantity=inventoryService.checkQuantity(f.getQuantity(),productId);
        double sellingPrice=productService.checkSellingPrice(f.getSellingPrice(),productId);
    }
    public void checkAll(List<CreateOrderForm> formList) throws ApiException {
        if (formList.size() == 0) {
            throw new ApiException("Cannot place empty order.");
        }
        for(CreateOrderForm form:formList) {
            check(form);
        }
    }

    public ResponseEntity<byte[]> getInvoicePDF( Integer id) throws Exception{
        return service.getInvoicePDF(id);
    }
}
