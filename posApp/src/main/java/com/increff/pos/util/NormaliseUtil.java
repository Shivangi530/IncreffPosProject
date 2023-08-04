package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;

public class NormaliseUtil {
    public static void normalize(ProductForm p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()).trim());
        p.setCategory(StringUtil.toLowerCase(p.getCategory()).trim());
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()).trim());
        p.setName(StringUtil.toLowerCase(p.getName()).trim());
    }

    public static void normalize(BrandForm p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()).trim());
        p.setCategory(StringUtil.toLowerCase(p.getCategory()).trim());
    }

    public static void normalize(InventoryForm p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()).trim());
    }
}
