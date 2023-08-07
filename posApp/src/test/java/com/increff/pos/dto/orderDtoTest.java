package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.helper.*;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertArrayEquals;

public class orderDtoTest extends AbstractUnitTest {

    @Autowired
    private brandDto brandDto;
    @Autowired
    private productDto productDto;
    @Autowired
    private inventoryDto inventoryDto;
    @Autowired
    private orderDto orderDto;
    @Autowired
    private orderItemDto orderItemDto;
    @Autowired
    private ProductService productService;

    @Test
    public void testAdd() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(f);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);

        List<CreateOrderForm> createOrderFormList= new ArrayList<>();
        CreateOrderForm createOrderForm= createOrderForm("barcode",10,5.0);
        createOrderFormList.add(createOrderForm);
        int orderId= orderDto.add(createOrderFormList);

        List<OrderItemData> data= orderItemDto.getOrderList(orderId);
        OrderItemData data1= data.get(0);

        String expectedBarcode= "barcode";
        int expectedQuantity=10;
        Double expectedSellingPrice=5.0;

        assertEquals(1,data.size());
        assertEquals(expectedBarcode,data1.getBarcode());
        assertEquals(expectedQuantity,(int)data1.getQuantity());
        assertEquals(expectedSellingPrice,data1.getSellingPrice(),0.01);
        assertEquals(orderId,(int)data1.getOrderId());
    }
    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm f = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(f);
        int id = productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm = inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id, inventoryForm);

        List<CreateOrderForm> createOrderFormList= new ArrayList<>();
        CreateOrderForm createOrderForm= createOrderForm("barcode",10,5.0);
        createOrderFormList.add(createOrderForm);
        int orderId= orderDto.add(createOrderFormList);

        orderDto.update(orderId,"INVOICED");
        assertEquals("INVOICED",orderDto.get(orderId).getStatus());
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm f = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(f);
        int id = productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm = inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id, inventoryForm);

        List<CreateOrderForm> createOrderFormList1= new ArrayList<>();
        CreateOrderForm createOrderForm1= createOrderForm("barcode",10,5.0);
        createOrderFormList1.add(createOrderForm1);
        int orderId1= orderDto.add(createOrderFormList1);

        List<CreateOrderForm> createOrderFormList2= new ArrayList<>();
        CreateOrderForm createOrderForm2= createOrderForm("barcode",100,7.2);
        createOrderFormList2.add(createOrderForm2);
        int orderId2= orderDto.add(createOrderFormList2);

        List<OrderData> list = orderDto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void testGetInvoicePDF() throws Exception {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm f = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(f);
        int id = productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm = inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id, inventoryForm);

        List<CreateOrderForm> createOrderFormList1= new ArrayList<>();
        CreateOrderForm createOrderForm1= createOrderForm("barcode",10,5.0);
        createOrderFormList1.add(createOrderForm1);
        int orderId1= orderDto.add(createOrderFormList1);
        ResponseEntity<byte[]> response = orderDto.getInvoicePDF(orderId1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
