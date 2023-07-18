package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class inventoryDto {
    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public InventoryData get(int id) throws ApiException {
        InventoryPojo p = service.get(id);
        String barcode=productService.selectBarcode(p.getId());
        int brandId=productService.get(id).getBrand_category();
        String brand= brandService.get(brandId).getBrand();
        String category= brandService.get(brandId).getCategory();
        return convert(p,barcode,brand,category);
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> list = service.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo p : list) {
            String barcode=productService.selectBarcode(p.getId());
            int brandId=productService.get(p.getId()).getBrand_category();
            String brand= brandService.get(brandId).getBrand();
            String category= brandService.get(brandId).getCategory();
            list2.add(convert(p,barcode,brand,category));
        }
        return list2;
    }

    public void update(int id, InventoryForm f) throws ApiException {
        InventoryPojo p = convert(f);
        service.update(id, p);
    }


    private InventoryData convert(InventoryPojo p,String barcode,String brand,String category) throws ApiException{
        InventoryData d = new InventoryData();
        d.setBarcode(barcode);
        d.setQuantity(p.getQuantity());
        d.setId(p.getId());
        d.setBrand(brand);
        d.setCategory(category);
        return d;
    }
    private static InventoryPojo convert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        return p;
    }
}