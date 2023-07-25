package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testUpdate() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm productForm= createProduct("brand","category","barcode",10.0,"name");
        productDto.add(productForm);

        InventoryForm f= createInventory("barcode",30);
        int id= productService.getIdByBarcode("barcode");
        dto.update(id,f);
        int expectedQuantity=30;
        assertEquals(expectedQuantity,dto.get(id).getQuantity());
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

    @Test(expected = ApiException.class)
    public void testInvalidQuantityUpdate() throws ApiException {
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm = createProduct("brand", "category", "barcode", 10.0, "name");
        productDto.add(productForm);

        InventoryForm f = createInventory("barcode", -1);
        int id = productService.getIdByBarcode("barcode");
        dto.update(id, f);
    }

}
