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

import static com.increff.pos.helper.helper.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class createOrderDtoTest extends AbstractUnitTest {

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
    public void testCheck() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(productForm);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);

        CreateOrderForm createOrderForm= createOrderForm("barcode",10,10.0);
        orderDto.check(createOrderForm);
    }

    @Test
    public void testCheckEmptyForm() throws ApiException{
        CreateOrderForm createOrderForm = null;
        try {
            orderDto.check(createOrderForm);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Cannot place empty order.", e.getMessage());
        }
    }

    @Test
    public void testCheckInvalidBarcode() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(productForm);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);

        CreateOrderForm createOrderForm= createOrderForm("barcode1",10,10.0);
        try {
            orderDto.check(createOrderForm);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Barcode doesn't exist", e.getMessage());
        }
    }

    @Test
    public void testCheckInvalidQuantity() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(productForm);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);

        CreateOrderForm createOrderForm= createOrderForm("barcode",1000,10.0);
        try {
            orderDto.check(createOrderForm);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Selected quantity:1000 is more than inventory: 200 for barcode:barcode", e.getMessage());
        }
    }

    @Test
    public void testCheckInvalidSellingPrice() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(productForm);
        int id=productService.getIdByBarcode("barcode");
        InventoryForm inventoryForm= inventoryDto.get(id);
        inventoryForm.setQuantity(200.0);
        inventoryDto.update(id,inventoryForm);

        CreateOrderForm createOrderForm= createOrderForm("barcode",10,20.0);
        try {
            orderDto.check(createOrderForm);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Selling price: 20.0 should be less than mrp: 10.0", e.getMessage());
        }
    }

    @Test
    public void testCheckAll() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm1= createProduct("brand","category","barcode1",10.0,"name1");
        productDto.add(productForm1);
        ProductForm productForm2= createProduct("brand","category","barcode2",20.0,"name2");
        productDto.add(productForm2);

        int id1=productService.getIdByBarcode("barcode1");
        InventoryForm inventoryForm1= inventoryDto.get(id1);
        inventoryForm1.setQuantity(200.0);
        inventoryDto.update(id1,inventoryForm1);

        int id2=productService.getIdByBarcode("barcode2");
        InventoryForm inventoryForm2= inventoryDto.get(id2);
        inventoryForm2.setQuantity(200.0);
        inventoryDto.update(id2,inventoryForm2);

        CreateOrderForm createOrderForm1= createOrderForm("barcode1",10,10.0);
        CreateOrderForm createOrderForm2= createOrderForm("barcode2",10,10.0);

        List<CreateOrderForm> list =new ArrayList<>();
        list.add(createOrderForm1);
        list.add(createOrderForm2);
        orderDto.checkAll(list);
    }

    @Test
    public void testCheckAllEmptyFormList() throws ApiException{
        List<CreateOrderForm> list =new ArrayList<>();
        try {
            orderDto.checkAll(list);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Cannot place empty order.", e.getMessage());
        }
    }
}
