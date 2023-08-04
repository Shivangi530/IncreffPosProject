package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private brandDto dto;
    @Autowired
    private BrandService service;

    @Test
    public void testAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        String expectedBrand="amul";
        String expectedCategory="milk";

        BrandPojo p= service.checkCombination(expectedBrand,expectedCategory);
        assertEquals(expectedBrand,p.getBrand());
        assertEquals(expectedCategory,p.getCategory());
    }

    @Test
    public void testEmptyBrandAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("");
        f.setCategory("Milk");
        try {
            dto.add(f);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Brand cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testEmptyCategoryAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("");
        try {
            dto.add(f);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            // ApiException is caught as expected
            assertEquals("Category cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testDuplicateBrandCategoryAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandForm f1= new BrandForm();
        f1.setBrand("Amul");
        f1.setCategory("Milk");
        try {
            dto.add(f1);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            // ApiException is caught as expected
            assertEquals("Brand:amul and Category:milk combination already exists", e.getMessage());
        }
    }

    @Test
    public void testGetBrand() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        String expectedBrand="amul";
        String expectedCategory="milk";

        BrandPojo brandPojo=service.checkCombination(expectedBrand,expectedCategory);

        BrandData p= dto.getBrand(brandPojo.getId());
        assertEquals(expectedBrand,p.getBrand());
        assertEquals(expectedCategory,p.getCategory());
    }

    @Test
    public void testInvalidGetBrand() throws ApiException{
        try {
            dto.getBrand(0);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            // ApiException is caught as expected
            assertEquals("Brand with given ID does not exit, id: 0", e.getMessage());
        }
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandForm f1= new BrandForm();
        f1.setBrand("Mother Dairy");
        f1.setCategory("Milk");
        dto.add(f1);

        List<BrandData> p= dto.getAll();
        assertEquals(2,p.size());
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        String expectedBrand="dairy milk";
        String expectedCategory="chocolate";

        BrandPojo p= service.checkCombination("amul","milk");

        f.setBrand("Dairy Milk");
        f.setCategory("Chocolate");
        dto.update(p.getId(),f);

        assertEquals(expectedBrand,p.getBrand());
        assertEquals(expectedCategory,p.getCategory());
    }

    @Test
    public void testEmptyBrandUpdate() throws ApiException {
        BrandForm f = new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandPojo p = service.checkCombination("amul", "milk");

        f.setBrand("");
        f.setCategory("Chocolate");
        try{
            dto.update(p.getId(), f);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Brand cannot be empty",e.getMessage());
        }

    }

    @Test
    public void testEmptyCategoryUpdate() throws ApiException {
        BrandForm f = new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandPojo p = service.checkCombination("amul", "milk");

        f.setBrand("Dairy Milk");
        f.setCategory("");
        try{
            dto.update(p.getId(), f);
            fail("Expected ApiException was not thrown");
        }catch (ApiException e){
            assertEquals("Category cannot be empty",e.getMessage());
        }
    }

    @Test
    public void testDuplicateBrandCategoryUpdate() throws ApiException {
        BrandForm f = new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandForm f1= new BrandForm();
        f1.setBrand("Dairy Milk");
        f1.setCategory("Chocolate");
        dto.add(f1);

        BrandPojo p = service.checkCombination("amul", "milk");

        f.setBrand("Dairy Milk");
        f.setCategory("Chocolate");

        try{
            dto.update(p.getId(), f);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Brand:dairy milk and Category:chocolate combination already exists",e.getMessage());
        }
    }

    @Test
    public void testAddList() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");

        BrandForm f1= new BrandForm();
        f1.setBrand("Mother Dairy");
        f1.setCategory("Milk");

        List<BrandForm> list= new ArrayList<>();
        list.add(f);
        list.add(f1);
        dto.addList(list);

        List<BrandData> p= dto.getAll();
        assertEquals(2,p.size());
    }

    @Test
    public void testAddEmptyList() throws ApiException{
        List<BrandForm> list= new ArrayList<>();
        try{
            dto.addList(list);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("List size cannot be zero",e.getMessage());
        }
    }

    @Test
    public void testAddListDuplicates() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");

        BrandForm f1= new BrandForm();
        f1.setBrand("Mother Dairy");
        f1.setCategory("Milk");

        BrandForm f2= new BrandForm();
        f2.setBrand("Mother Dairy");
        f2.setCategory("Milk");

        List<BrandForm> list= new ArrayList<>();
        list.add(f);
        list.add(f1);
        list.add(f2);

        try{
            dto.addList(list);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("[2=Duplicate entry for Brand: mother dairy Category: milk]",e.getMessage());
        }
    }
}
