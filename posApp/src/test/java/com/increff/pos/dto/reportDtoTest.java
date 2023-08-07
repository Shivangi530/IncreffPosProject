package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.increff.pos.helper.helper.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class reportDtoTest extends AbstractUnitTest {

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
    private reportsDto dto;
    @Autowired
    private ProductService service;

    @Test
    public void testGetAllInventory() throws ApiException{
        BrandForm brandForm = createBrand("brand", "category");
        brandDto.add(brandForm);
        ProductForm productForm1 = createProduct("brand", "category", "barcode1", 10.0, "name");
        productDto.add(productForm1);
        ProductForm productForm2 = createProduct("brand", "category", "barcode2", 10.0, "name");
        productDto.add(productForm2);

        List<InventoryReportData> list= dto.getAllInventory();
        assertEquals(2,list.size());
    }

//    @Test
//    public void testAdd() throws ApiException{
//        BrandForm brandForm= createBrand("brand","category");
//        brandDto.add(brandForm);
//        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
//        productDto.add(f);
//        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");
//        productDto.add(f1);
//
//        int id=service.getIdByBarcode("barcode1");
//        InventoryForm inventoryForm= inventoryDto.get(id);
//        inventoryForm.setQuantity(200);
//        inventoryDto.update(id,inventoryForm);
//
//        int orderId= orderDto.add();
//        OrderItemForm orderItemForm= createOrderItem(orderId,"barcode1",10,5.0);
//        orderItemDto.add(orderItemForm);
//
//        orderDto.update(orderId,"INVOICED");
//
//        SalesReportData salesReportData= new SalesReportData();
//        LocalDate startDate = LocalDate.now().minusMonths(1);
//        LocalDate endDate = LocalDate.now();
//        salesReportData.setStartDate(startDate);
//        salesReportData.setEndDate(endDate);
//        List<SalesReportData> dataList= dto.add(salesReportData);
//        assertEquals(1,dataList.size());
//    }
//
//    @Test
//    public void testGetAllDailySales() throws ApiException{
//        BrandForm brandForm= createBrand("brand","category");
//        brandDto.add(brandForm);
//        ProductForm f= createProduct("brand","category","barcode1",10.0,"name1");
//        productDto.add(f);
//        ProductForm f1= createProduct("brand","category","barcode2",20.0,"name2");
//        productDto.add(f1);
//
//        int id=service.getIdByBarcode("barcode1");
//        InventoryForm inventoryForm= inventoryDto.get(id);
//        inventoryForm.setQuantity(200);
//        inventoryDto.update(id,inventoryForm);
//
//        int orderId= orderDto.add();
//        OrderItemForm orderItemForm= createOrderItem(orderId,"barcode1",10,5.0);
//        orderItemDto.add(orderItemForm);
//
//        orderDto.update(orderId,"INVOICED");
//
////        List<OrderData> orderlist= orderDto.getAll();
////        System.out.println("size= "+orderlist.size());
////        System.out.println(orderlist.get(0).getStatus());
//
//        List<DayOnDaySalesReportData> list= dto.getAllDailySales();
//        assertEquals(1,list.size());
//    }

}
