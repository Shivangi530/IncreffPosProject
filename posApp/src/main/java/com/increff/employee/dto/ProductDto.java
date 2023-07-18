package com.increff.employee.dto;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.increff.employee.util.ConversionUtil.convert;
import static com.increff.employee.util.NormaliseUtil.normalize;

@Component
public class ProductDto {
    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm f) throws ApiException {
        // TODO normalize function
        normalize(f);
        int brandCategoryId=0;
        if(brandService.checkCombination(f.getBrand(),f.getCategory())!=null) {
            brandCategoryId=brandService.checkCombination(f.getBrand(),f.getCategory()).getId();
        }
        ProductPojo p = convert(f,brandCategoryId);
        service.add(p);
        InventoryPojo pi = new InventoryPojo();
        pi.setId(p.getId());
        pi.setQuantity(0);
        inventoryService.add(pi);
    }
    public ProductData get(int id) throws ApiException {
        ProductPojo p = service.get(id);
        String brand=brandService.get(p.getBrand_category()).getBrand();
        String category=brandService.get(p.getBrand_category()).getCategory();
        return convert(p,brand,category);
    }
    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for (ProductPojo p : list) {
            String brand=brandService.getValueBrandCategory(p.getBrand_category()).getBrand();
            String category=brandService.getValueBrandCategory(p.getBrand_category()).getCategory();
            list2.add(convert(p,brand,category));
        }
        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {
        normalize(f);
        int brandCategory=0;
        if(brandService.checkCombination(f.getBrand(),f.getCategory())!=null) {
            brandCategory=brandService.checkCombination(f.getBrand(),f.getCategory()).getId();
        }
        ProductPojo p = convert(f,brandCategory);
        service.update(id, p);
    }

//    private ProductData convert(ProductPojo p,String brand,String category) {
//        ProductData d = new ProductData();
//        d.setBarcode(p.getBarcode());
//        d.setBrand_category(p.getBrand_category());
//        d.setBrand(brand);
//        d.setCategory(category);
//        d.setMrp(p.getMrp());
//        d.setName(p.getName());
//        d.setId(p.getId());
//        return d;
//    }
//
//    private ProductPojo convert(ProductForm f,int brandCategory) {
//        ProductPojo p = new ProductPojo();
//        p.setBarcode(f.getBarcode());
//        p.setName(f.getName());
//        p.setMrp(f.getMrp());
//        p.setBrand_category(brandCategory);
//        return p;
//    }
//
//    protected static void normalize(ProductForm f) {
//        f.setBrand(StringUtil.toLowerCase(f.getBrand()).trim());
//        f.setCategory(StringUtil.toLowerCase(f.getCategory()).trim());
//    }
}
