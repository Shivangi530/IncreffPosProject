package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BrandDtoTest2 extends AbstractUnitTest {

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

    @Test(expected = ApiException.class)
    public void testEmptyBrandAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("");
        f.setCategory("Milk");
        dto.add(f);
    }

    @Test(expected = ApiException.class)
    public void testEmptyCategoryAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("");
        dto.add(f);
    }

    @Test(expected = ApiException.class)
    public void testDuplicateBrandCategoryAdd() throws ApiException{
        BrandForm f= new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandForm f1= new BrandForm();
        f1.setBrand("Amul");
        f1.setCategory("Milk");
        dto.add(f1);
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

    @Test(expected = ApiException.class)
    public void testInvalidGetBrand() throws ApiException{
        dto.getBrand(0);
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

    @Test(expected = ApiException.class)
    public void testEmptyBrandUpdate() throws ApiException {
        BrandForm f = new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandPojo p = service.checkCombination("amul", "milk");

        f.setBrand("");
        f.setCategory("Chocolate");
        dto.update(p.getId(), f);
    }

    @Test(expected = ApiException.class)
    public void testEmptyCategoryUpdate() throws ApiException {
        BrandForm f = new BrandForm();
        f.setBrand("Amul");
        f.setCategory("Milk");
        dto.add(f);

        BrandPojo p = service.checkCombination("amul", "milk");

        f.setBrand("Dairy Milk");
        f.setCategory("");
        dto.update(p.getId(), f);
    }

    @Test(expected = ApiException.class)
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
        dto.update(p.getId(), f);
    }

}
