package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        Integer expectedQuantity=10;
        Double expectedSellingPrice=5.0;

        assertEquals(expectedBarcode,data1.getBarcode());
        assertEquals((int)expectedQuantity,data1.getQuantity());
        assertEquals(expectedSellingPrice,data1.getSellingPrice(),0.01);
        assertEquals(orderId,data1.getOrderId());
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
        productDto.add(f);
        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");
        productDto.add(f1);

//        List<ProductData> dataList= dto.getAll();
//        assertEquals(2,dataList.size());
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm brandForm= createBrand("brand","category");
        brandDto.add(brandForm);
        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
        productDto.add(f);

        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");

        int id=productService.getIdByBarcode("barcode1");
        productDto.update(id,f1);

        String expectedBrand="brand";
        String expectedCategory="category";
        String expectedBarcode="barcode2";
        String expectedName="name2";
        Double expectedMrp=20.0;

        ProductData data= productDto.get(id);

        assertEquals(expectedBrand,data.getBrand());
        assertEquals(expectedCategory,data.getCategory());
        assertEquals(expectedBarcode,data.getBarcode());
        assertEquals(expectedName,data.getName());
        assertEquals(expectedMrp,data.getMrp(),0.01);
    }



}
