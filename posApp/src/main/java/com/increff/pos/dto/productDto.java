package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;

@Component
public class productDto {
    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm f) throws ApiException {
        normalize(f);
        validate(f);
        int brandCategoryId = 0;
        if (brandService.checkCombination(f.getBrand(), f.getCategory()) != null) {
            brandCategoryId = brandService.checkCombination(f.getBrand(), f.getCategory()).getId();
        }
        ProductPojo p = convert(f, brandCategoryId);
        service.add(p);
        InventoryPojo pi = new InventoryPojo(); // todo: to create the inventory pojo in the service layer
        pi.setId(p.getId());
        pi.setQuantity(0);
        inventoryService.add(pi);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = service.get(id);
        String brand = brandService.get(p.getBrand_category()).getBrand();
        String category = brandService.get(p.getBrand_category()).getCategory();
        return convert(p, brand, category);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for (ProductPojo p : list) {
            String brand = brandService.getValueBrandCategory(p.getBrand_category()).getBrand(); //todo: to call once by using the list api
            String category = brandService.getValueBrandCategory(p.getBrand_category()).getCategory();
            list2.add(convert(p, brand, category));
        }
        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {
        normalize(f);
        validate(f);
        int brandCategory = 0;
        if (brandService.checkCombination(f.getBrand(), f.getCategory()) != null) {
            brandCategory = brandService.checkCombination(f.getBrand(), f.getCategory()).getId();
        }
        ProductPojo p = convert(f, brandCategory);
        service.update(id, p);
    }
}