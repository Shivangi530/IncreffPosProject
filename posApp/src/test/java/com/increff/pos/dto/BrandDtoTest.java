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
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        String expectedBrand="amul";
        String expectedCategory="milk";

        BrandPojo pojo= service.checkCombination(expectedBrand,expectedCategory);
        assertEquals(expectedBrand,pojo.getBrand());
        assertEquals(expectedCategory,pojo.getCategory());
    }

    @Test
    public void testEmptyBrandAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("");
        form.setCategory("Milk");
        try {
            dto.add(form);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals( "Brand cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testEmptyCategoryAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("");
        try {
            dto.add(form);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals("Category cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testDuplicateBrandCategoryAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        BrandForm form1= new BrandForm();
        form1.setBrand("Amul");
        form1.setCategory("Milk");
        try {
            dto.add(form1);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e) {
            assertEquals("Brand:amul and Category:milk combination already exists", e.getMessage());
        }
    }

    @Test
    public void testGetBrand() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        String expectedBrand="amul";
        String expectedCategory="milk";

        BrandPojo brandPojo=service.checkCombination(expectedBrand,expectedCategory);

        BrandData pojo= dto.getBrand(brandPojo.getId());
        assertEquals(expectedBrand,pojo.getBrand());
        assertEquals(expectedCategory,pojo.getCategory());
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
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        BrandForm form1= new BrandForm();
        form1.setBrand("Mother Dairy");
        form1.setCategory("Milk");
        dto.add(form1);

        List<BrandData> pojo= dto.getAll();
        assertEquals(2,pojo.size());
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        String expectedBrand="dairy milk";
        String expectedCategory="chocolate";

        BrandPojo pojo= service.checkCombination("amul","milk");

        form.setBrand("Dairy Milk");
        form.setCategory("Chocolate");
        dto.update(pojo.getId(),form);

        assertEquals(expectedBrand,pojo.getBrand());
        assertEquals(expectedCategory,pojo.getCategory());
    }

    @Test
    public void testEmptyBrandUpdate() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        BrandPojo pojo = service.checkCombination("amul", "milk");

        form.setBrand("");
        form.setCategory("Chocolate");
        try{
            dto.update(pojo.getId(), form);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Brand cannot be empty",e.getMessage());
        }

    }

    @Test
    public void testEmptyCategoryUpdate() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        BrandPojo pojo = service.checkCombination("amul", "milk");

        form.setBrand("Dairy Milk");
        form.setCategory("");
        try{
            dto.update(pojo.getId(), form);
            fail("Expected ApiException was not thrown");
        }catch (ApiException e){
            assertEquals("Category cannot be empty",e.getMessage());
        }
    }

    @Test
    public void testDuplicateBrandCategoryUpdate() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");
        dto.add(form);

        BrandForm form1= new BrandForm();
        form1.setBrand("Dairy Milk");
        form1.setCategory("Chocolate");
        dto.add(form1);

        BrandPojo pojo = service.checkCombination("amul", "milk");

        form.setBrand("Dairy Milk");
        form.setCategory("Chocolate");

        try{
            dto.update(pojo.getId(), form);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("Brand:dairy milk and Category:chocolate combination already exists",e.getMessage());
        }
    }

    @Test
    public void testAddList() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");

        BrandForm form1= new BrandForm();
        form1.setBrand("Mother Dairy");
        form1.setCategory("Milk");

        List<BrandForm> list= new ArrayList<>();
        list.add(form);
        list.add(form1);
        dto.addList(list);

        List<BrandData> pojo= dto.getAll();
        assertEquals(2,pojo.size());
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
        BrandForm form= new BrandForm();
        form.setBrand("Amul");
        form.setCategory("Milk");

        BrandForm form1= new BrandForm();
        form1.setBrand("Mother Dairy");
        form1.setCategory("Milk");

        BrandForm form2= new BrandForm();
        form2.setBrand("Mother Dairy");
        form2.setCategory("Milk");

        List<BrandForm> list= new ArrayList<>();
        list.add(form);
        list.add(form1);
        list.add(form2);

        try{
            dto.addList(list);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("[2=Duplicate entry for Brand: mother dairy Category: milk]",e.getMessage());
        }
    }
}
