package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.increff.pos.helper.helper.*;
import static junit.framework.TestCase.assertEquals;

public class orderItemDtoTest extends AbstractUnitTest {

    @Autowired
    private brandDto brandDto;
    @Autowired
    private productDto productDto;
    @Autowired
    private inventoryDto inventoryDto;
    @Autowired
    private orderDto orderDto;
    @Autowired
    private orderItemDto dto;
    @Autowired
    private ProductService productService;

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

        List<OrderItemData> data= dto.getOrderList(orderId);
        int orderItemId = data.get(0).getId();

        OrderItemForm orderItemFormUpdated = createOrderItem(orderId, "barcode", 100, 9.0);
        dto.update(orderItemId, orderItemFormUpdated);

        OrderItemData data1 = dto.getOrderList(orderId).get(0);

        int expectedQuantity = 100;
        Double expectedSellingPrice = 9.0;

        assertEquals(expectedQuantity, (int) data1.getQuantity());
        assertEquals(expectedSellingPrice, data1.getSellingPrice(), 0.01);
        assertEquals(orderId, (int) data1.getOrderId());
    }

    @Test
    public void testUpdateInvoiced() throws ApiException {
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

        List<OrderItemData> data= dto.getOrderList(orderId);
        int orderItemId = data.get(0).getId();

        orderDto.update(orderId,"INVOICED");

        OrderItemForm orderItemFormUpdated = createOrderItem(orderId, "barcode", 100, 50.0);

        try{
            dto.update(orderItemId, orderItemFormUpdated);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Invalid Operation: Invoice has been generated for the order. ",e.getMessage());
        }
    }

    @Test
    public void testUpdateInvalidQuantity() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm f = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(f);
        int id = productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm = inventoryDto.get(id);
        inventoryForm.setQuantity(20.0);
        inventoryDto.update(id, inventoryForm);

        List<CreateOrderForm> createOrderFormList= new ArrayList<>();
        CreateOrderForm createOrderForm= createOrderForm("barcode",10,5.0);
        createOrderFormList.add(createOrderForm);
        int orderId= orderDto.add(createOrderFormList);

        List<OrderItemData> data= dto.getOrderList(orderId);
        int orderItemId = data.get(0).getId();

        OrderItemForm orderItemFormUpdated = createOrderItem(orderId, "barcode", 100, 50.0);
        try{
            dto.update(orderItemId, orderItemFormUpdated);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Selected quantity:100 is more than inventory: 10 for barcode:barcode",e.getMessage());
        }
    }

    @Test
    public void testDelete() throws ApiException {
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

        List<OrderItemData> data= dto.getOrderList(orderId);
        assertEquals(1, data.size());

        dto.delete(data.get(0).getId());

        List<OrderItemData> dataUpdated = dto.getOrderList(orderId);
        assertEquals(0, dataUpdated.size());
    }

    @Test
    public void testDeleteInvoiced() throws ApiException {
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

        List<OrderItemData> data= dto.getOrderList(orderId);

        orderDto.update(orderId,"INVOICED");
        try{
            dto.delete(data.get(0).getId());
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Invalid Operation: Invoice has been generated for the order. ",e.getMessage());
        }
    }

    @Test
    public void testGetOrderList() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(f);
        ProductForm f1= createProduct("brand","category","barcode1",60.0,"name");
        productDto.add(f1);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);
        int id2=productService.getIdByBarcode("barcode1");
        InventoryForm inventoryForm2= inventoryDto.get(id2);
        inventoryForm2.setQuantity(200.0);
        inventoryDto.update(id2,inventoryForm2);

        List<CreateOrderForm> createOrderFormList= new ArrayList<>();
        CreateOrderForm createOrderForm1= createOrderForm("barcode",10,5.0);
        CreateOrderForm createOrderForm2= createOrderForm("barcode1",100,50.0);
        createOrderFormList.add(createOrderForm1);
        createOrderFormList.add(createOrderForm2);
        int orderId= orderDto.add(createOrderFormList);

        List<OrderItemData> data= dto.getOrderList(orderId);
        assertEquals(2,data.size());
    }
}
