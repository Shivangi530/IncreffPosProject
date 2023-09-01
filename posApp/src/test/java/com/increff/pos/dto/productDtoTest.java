package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductTsvForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.helper.createBrand;
import static com.increff.pos.helper.helper.createProduct;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

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
        String expectedName="name2";
        Double expectedMrp=20.0;

        ProductData data= dto.get(id);

        assertEquals(expectedBrand,data.getBrand());
        assertEquals(expectedCategory,data.getCategory());
        assertEquals(expectedName,data.getName());
        assertEquals(expectedMrp,data.getMrp(),0.01);
    }

    @Test
    public void tesAddList() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductTsvForm f= createProduct("brand","category","barcode1","10.0","name1");
        ProductTsvForm f1= createProduct("brand","category","barcode2","20.0","name2");
        List<ProductTsvForm> formList= new ArrayList<>();
        formList.add(f);
        formList.add(f1);
        dto.addList(formList);

        List<ProductData> dataList= dto.getAll();
        assertEquals(2,dataList.size());
    }

    @Test
    public void tesAddEmptyList() throws ApiException{
        List<ProductTsvForm> formList= new ArrayList<>();
        try{
            dto.addList(formList);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("List size cannot be zero",e.getMessage());
        }
    }

    @Test
    public void tesAddListDuplicate() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductTsvForm f= createProduct("brand","category","barcode1","10.0","name1");
        ProductTsvForm f1= createProduct("brand","category","barcode2","20.0","name2");
        ProductTsvForm f2= createProduct("brand","category","barcode2","20.0","name2");
        List<ProductTsvForm> formList= new ArrayList<>();
        formList.add(f);
        formList.add(f1);
        formList.add(f2);
        try{
            dto.addList(formList);
            fail("Expected ApiException was not thrown");
        } catch (ApiException e){
            assertEquals("[2=Duplicate entry for Barcode: barcode2]",e.getMessage());
        }
    }

}
