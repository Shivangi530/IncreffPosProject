package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;

public class NormaliseUtil {
    public static void normalize(ProductForm pojo) {
        pojo.setBrand(StringUtil.toLowerCase(pojo.getBrand()).trim());
        pojo.setCategory(StringUtil.toLowerCase(pojo.getCategory()).trim());
        pojo.setBarcode(StringUtil.toLowerCase(pojo.getBarcode()).trim());
        pojo.setName(StringUtil.toLowerCase(pojo.getName()).trim());
        pojo.setMrp((int) (pojo.getMrp() * 100) / 100.0);
    }

    public static void normalize(BrandForm pojo) {
        pojo.setBrand(StringUtil.toLowerCase(pojo.getBrand()).trim());
        pojo.setCategory(StringUtil.toLowerCase(pojo.getCategory()).trim());
    }

    public static void normalize(InventoryForm pojo) {
        pojo.setBarcode(StringUtil.toLowerCase(pojo.getBarcode()).trim());
    }
}
