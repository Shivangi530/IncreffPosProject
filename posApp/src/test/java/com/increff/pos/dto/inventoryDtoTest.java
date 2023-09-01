package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.helper.*;
import static junit.framework.TestCase.assertEquals;

public class inventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private brandDto brandDto;
    @Autowired
    private productDto productDto;
    @Autowired
    private inventoryDto dto;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService service;

    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(productForm);

        InventoryForm f = createInventory("barcode", 30);
        int id = productService.getIdByBarcode("barcode");
        dto.update(id, f);
        int expectedQuantity = 30;
        assertEquals( expectedQuantity, dto.get(id).getQuantity(),0.01);
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f1= createProduct("brand","category","barcode1",10.0,"name1");
        productDto.add(f1);
        ProductForm f2= createProduct("brand","category","barcode2",20.0,"name2");
        productDto.add(f2);

        List<InventoryData> list= dto.getAll();
        assertEquals(2,list.size());
    }

    @Test
    public void testInvalidQuantityUpdate() throws ApiException, JsonProcessingException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(productForm);

        InventoryForm f = createInventory("barcode", -1);
        int id = productService.getIdByBarcode("barcode");
        try{
            dto.update(id, f);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Quantity cannot be negative",e.getMessage());
        }
    }

    @Test
    public void testUpdateList() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm1 = createProduct("brand", "category", "barcode1", 10.0, "name");
        productDto.add(productForm1);
        ProductForm productForm2 = createProduct("brand", "category", "barcode2", 10.0, "name");
        productDto.add(productForm2);

        InventoryTsvForm f1 = createInventory("barcode1", "30");
        InventoryTsvForm f2 = createInventory("barcode2", "40");

        List<InventoryTsvForm> list= new ArrayList<>();
        list.add(f1);
        list.add(f2);

        dto.updateList(list);

        List<InventoryData> listData= dto.getAll();
        assertEquals( 30,listData.get(0).getQuantity(),0.01);
        assertEquals( 40, listData.get(1).getQuantity(),0.01);
    }

    @Test
    public void testUpdateListInvalidBarcode() throws ApiException{
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm1 = createProduct("brand", "category", "barcode1", 10.0, "name");
        productDto.add(productForm1);
        ProductForm productForm2 = createProduct("brand", "category", "barcode2", 10.0, "name");
        productDto.add(productForm2);

        InventoryTsvForm f1 = createInventory("barcode1", "30");
        InventoryTsvForm f2 = createInventory("barcode2", "40");
        InventoryTsvForm f3 = createInventory("barcode", "40");

        List<InventoryTsvForm> list= new ArrayList<>();
        list.add(f1);
        list.add(f2);
        list.add(f3);

        try{
            dto.updateList(list);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("[2=Barcode doesn't exist]",e.getMessage());
        }
    }

    @Test
    public void testUpdateEmptyList() {
        List<InventoryTsvForm> list= new ArrayList<>();
        try{
            dto.updateList(list);
            Assert.fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("List size cannot be zero",e.getMessage());
        }
    }
}
