package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testAdd() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(f);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200);
        inventoryDto.update(id,inventoryForm);

        int orderId= orderDto.add();

        OrderItemForm orderItemForm= createOrderItem(orderId,"barcode",10,5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data= dto.getAll();
        OrderItemData data1= data.get(0);

        String expectedBarcode= "barcode";
        int expectedQuantity=10;
        Double expectedSellingPrice=5.0;

        assertEquals(expectedBarcode,data1.getBarcode());
        assertEquals(expectedQuantity,(int)data1.getQuantity());
        assertEquals(expectedSellingPrice,data1.getSellingPrice(),0.01);
        assertEquals(orderId,(int)data1.getOrderId());
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
        productDto.add(f);
        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");
        productDto.add(f1);

    }

    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm f = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(f);
        int id = productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm = inventoryDto.get(id);
        inventoryForm.setQuantity(200);
        inventoryDto.update(id, inventoryForm);

        int orderId = orderDto.add();

        OrderItemForm orderItemForm = createOrderItem(orderId, "barcode", 10, 5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data = dto.getAll();
        int orderItemId = data.get(0).getId();

        OrderItemForm orderItemFormUpdated = createOrderItem(orderId, "barcode", 100, 50.0);
        dto.update(orderItemId, orderItemFormUpdated);

        OrderItemData data1 = dto.get(orderItemId);

        int expectedQuantity = 100;
        Double expectedSellingPrice = 50.0;

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
        inventoryForm.setQuantity(200);
        inventoryDto.update(id, inventoryForm);

        int orderId = orderDto.add();

        OrderItemForm orderItemForm = createOrderItem(orderId, "barcode", 10, 5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data = dto.getAll();
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
        inventoryForm.setQuantity(20);
        inventoryDto.update(id, inventoryForm);

        int orderId = orderDto.add();

        OrderItemForm orderItemForm = createOrderItem(orderId, "barcode", 10, 5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data = dto.getAll();
        int orderItemId = data.get(0).getId();

        OrderItemForm orderItemFormUpdated = createOrderItem(orderId, "barcode", 100, 50.0);
        try{
            dto.update(orderItemId, orderItemFormUpdated);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Invalid operation",e.getMessage());
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
        inventoryForm.setQuantity(200);
        inventoryDto.update(id, inventoryForm);

        int orderId = orderDto.add();

        OrderItemForm orderItemForm = createOrderItem(orderId, "barcode", 10, 5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data = dto.getAll();
        assertEquals(1, data.size());

        dto.delete(data.get(0).getId());

        List<OrderItemData> dataUpdated = dto.getAll();
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
        inventoryForm.setQuantity(200);
        inventoryDto.update(id, inventoryForm);

        int orderId = orderDto.add();

        OrderItemForm orderItemForm = createOrderItem(orderId, "barcode", 10, 5.0);
        dto.add(orderItemForm);

        List<OrderItemData> data = dto.getAll();

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
        ProductForm f1= createProduct("brand","category","barcode1",10.0,"name");
        productDto.add(f1);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200);
        inventoryDto.update(id,inventoryForm);
        int id2=productService.getIdByBarcode("barcode1");
        InventoryForm inventoryForm2= inventoryDto.get(id2);
        inventoryForm2.setQuantity(200);
        inventoryDto.update(id2,inventoryForm2);

        int orderId= orderDto.add();

        OrderItemForm orderItemForm= createOrderItem(orderId,"barcode",10,5.0);
        dto.add(orderItemForm);
        OrderItemForm orderItemForm2= createOrderItem(orderId,"barcode1",100,50.0);
        dto.add(orderItemForm2);

        List<OrderItemData> data= dto.getOrderList(orderId);

        assertEquals(2,data.size());
    }
}
