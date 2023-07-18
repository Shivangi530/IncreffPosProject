package com.increff.employee.util;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

public class ConversionUtil {

    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setCategory(p.getCategory());
        d.setBrand(p.getBrand());
        d.setId(p.getId());
        return d;
    }

    public static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setCategory(f.getCategory());
        p.setBrand(f.getBrand());
        return p;
    }
    public static ProductData convert(ProductPojo p, String brand, String category) {
        ProductData d = new ProductData();
        d.setBarcode(p.getBarcode());
        d.setBrand_category(p.getBrand_category());
        d.setBrand(brand);
        d.setCategory(category);
        d.setMrp(p.getMrp());
        d.setName(p.getName());
        d.setId(p.getId());
        return d;
    }

    public static ProductPojo convert(ProductForm f, int brandCategory) {
        ProductPojo p = new ProductPojo();
        p.setBarcode(f.getBarcode());
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        p.setBrand_category(brandCategory);
        return p;
    }

}
