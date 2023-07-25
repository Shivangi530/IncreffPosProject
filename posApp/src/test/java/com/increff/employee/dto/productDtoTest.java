package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import java.util.List;

import static com.increff.employee.helper.helper.createBrand;
import static com.increff.employee.helper.helper.createProduct;
import static junit.framework.TestCase.assertEquals;

public class productDtoTest extends AbstractUnitTest {

    @Autowired
    private brandDto brandDto;
    @Autowired
    private productDto dto;
    @Autowired
    private ProductService service;

    @Test
    public void testAdd() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode",10.0,"name");
        dto.add(f);

        String expectedBrand="brand";
        String expectedCategory="category";
        String expectedBarcode="barcode";
        String expectedName="name";
        Double expectedMrp=10.0;

        int id= service.getIdByBarcode(expectedBarcode);
        ProductData data= dto.get(id);

        assertEquals(expectedBrand,data.getBrand());
        assertEquals(expectedCategory,data.getCategory());
        assertEquals(expectedBarcode,data.getBarcode());
        assertEquals(expectedName,data.getName());
        assertEquals(expectedMrp,data.getMrp(),0.01);
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
        dto.add(f);
        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");
        dto.add(f1);

        List<ProductData> dataList= dto.getAll();
        assertEquals(2,dataList.size());
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
        dto.add(f);

        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");

        int id=service.getIdByBarcode("barcode1");
        dto.update(id,f1);

        String expectedBrand="brand";
        String expectedCategory="category";
        String expectedBarcode="barcode2";
        String expectedName="name2";
        Double expectedMrp=20.0;

        ProductData data= dto.get(id);

        assertEquals(expectedBrand,data.getBrand());
        assertEquals(expectedCategory,data.getCategory());
        assertEquals(expectedBarcode,data.getBarcode());
        assertEquals(expectedName,data.getName());
        assertEquals(expectedMrp,data.getMrp(),0.01);
    }



}
