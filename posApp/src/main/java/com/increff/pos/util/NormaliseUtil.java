package com.increff.pos.util;

import com.increff.pos.model.*;

public class NormaliseUtil {
    public static void normalize(ProductForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
        form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()).trim());
        form.setName(StringUtil.toLowerCase(form.getName()).trim());
        form.setMrp((int) (form.getMrp() * 100) / 100.0);
    }

    public static void normalize(BrandForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
        form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
    }

    public static void normalize(InventoryForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()).trim());
    }

    public static void normalize(ProductTsvForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
        form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()).trim());
        form.setName(StringUtil.toLowerCase(form.getName()).trim());
    }

    public static void normalize(InventoryTsvForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()).trim());
    }
}
