package com.increff.employee.util;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

public class NormaliseUtil {
//    public static void normalize(ProductPojo p) {
//        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()).trim());
//        p.setName(StringUtil.toLowerCase(p.getName()).trim()    );
//    }
    public static void normalize(ProductForm p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()).trim());
        p.setName(StringUtil.toLowerCase(p.getName()).trim()    );
    }

//    public static void normalize(BrandPojo p) {
//        p.setBrand(StringUtil.toLowerCase(p.getBrand()).trim());
//        p.setCategory(StringUtil.toLowerCase(p.getCategory()).trim());
//    }
    public static void normalize(BrandForm p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()).trim());
        p.setCategory(StringUtil.toLowerCase(p.getCategory()).trim());
    }
}
