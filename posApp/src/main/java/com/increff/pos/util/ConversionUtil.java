package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;

import java.time.LocalDate;

public class ConversionUtil {

    public static BrandData convert(BrandPojo pojo) {
        BrandData data = new BrandData();
        data.setCategory(pojo.getCategory());
        data.setBrand(pojo.getBrand());
        data.setId(pojo.getId());
        return data;
    }

    public static BrandPojo convert(BrandForm form) {
        BrandPojo pojo = new BrandPojo();
        pojo.setCategory(form.getCategory());
        pojo.setBrand(form.getBrand());
        return pojo;
    }
    public static ProductData convert(ProductPojo pojo, String brand, String category) {
        ProductData data = new ProductData();
        data.setBarcode(pojo.getBarcode());
        data.setBrandCategory(pojo.getBrandCategory());
        data.setBrand(brand);
        data.setCategory(category);
        data.setMrp(pojo.getMrp());
        data.setName(pojo.getName());
        data.setId(pojo.getId());
        return data;
    }

    public static ProductPojo convert(ProductForm form, Integer brandCategory) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setName(form.getName());
        pojo.setMrp(form.getMrp());
        pojo.setBrandCategory(brandCategory);
        return pojo;
    }

    public static ProductForm convert(ProductTsvForm tsvForm,Double mrp) {
        ProductForm form = new ProductForm();
        form.setBarcode(tsvForm.getBarcode());
        form.setName(tsvForm.getName());
        form.setMrp(mrp);
        form.setCategory(tsvForm.getCategory());
        form.setBrand(tsvForm.getBrand());
        return form;
    }

    public static InventoryData convert(InventoryPojo pojo, String barcode) throws ApiException {
        InventoryData data = new InventoryData();
        data.setBarcode(barcode);
        data.setQuantity(Double.valueOf(pojo.getQuantity()));
        data.setId(pojo.getId());
        return data;
    }
    public static InventoryPojo convert(InventoryForm form) {
        InventoryPojo pojo = new InventoryPojo();
        int quantityInt = form.getQuantity() != null ? form.getQuantity().intValue() : 0;
        pojo.setQuantity(quantityInt);
        return pojo;
    }

    public static InventoryReportData convert(InventoryPojo pojo,String barcode,String brand,String category) throws ApiException{
        InventoryReportData data = new InventoryReportData();
        data.setBarcode(barcode);
        data.setQuantity(pojo.getQuantity());
        data.setId(pojo.getId());
        data.setBrand(brand);
        data.setCategory(category);
        return data;
    }

    public static OrderItemData convert(OrderItemPojo pojo,String barcode) {
        OrderItemData data = new OrderItemData();
        data.setOrderId(pojo.getOrderId());
        data.setBarcode(barcode);
        data.setProductId(pojo.getProductId());
        data.setQuantity(pojo.getQuantity());
        data.setSellingPrice(pojo.getSellingPrice());
        data.setId(pojo.getId());
        return data;
    }
    public static OrderItemData convert(OrderItemPojo pojo,String productName, String barcode){
        OrderItemData data = new OrderItemData();
        data.setOrderId(pojo.getOrderId());
        data.setProductId(pojo.getProductId());
        data.setQuantity(pojo.getQuantity());
        data.setSellingPrice(pojo.getSellingPrice());
        data.setId(pojo.getId());
        data.setProductName(productName);
        data.setBarcode(barcode);
        return data;
    }
    public static OrderData convert(OutwardOrderPojo pojo) {
        OrderData data = new OrderData();
        data.setId(pojo.getId());
        data.setStatus(String.valueOf(pojo.getStatus()));
        data.setDateTime(pojo.getDateTime());
        return data;
    }
    public static UserData convert(UserPojo pojo) {
        UserData data = new UserData();
        data.setEmail(pojo.getEmail());
        data.setRole(pojo.getRole().name());
        data.setId(pojo.getId());
        return data;
    }

    public static UserPojo convert(UserForm form) {
        UserPojo pojo = new UserPojo();
        pojo.setEmail(form.getEmail());
        pojo.setRole(EnumData.Role.valueOf(form.getRole()));
        pojo.setPassword(form.getPassword());
        return pojo;
    }

    public static DayOnDaySalesReportData convert(DayOnDaySalesPojo pojo) {
        DayOnDaySalesReportData data = new DayOnDaySalesReportData();
        data.setRevenue(pojo.getRevenue());
        data.setOrderCount(pojo.getOrderCount());
        data.setDate(pojo.getDate());
        data.setItemCount(pojo.getItemCount());
        return data;
    }

    public static SalesReportData convert(SalesReportData data,OrderItemPojo pojo) {
        data.setQuantity(data.getQuantity()+pojo.getQuantity());
        double revenue= data.getRevenue()+(pojo.getSellingPrice()*pojo.getQuantity());
        revenue=Math.round(revenue  * 100.0) / 100.0;
        data.setRevenue(revenue);
        return data;
    }

    public static SalesReportData convert(OrderItemPojo pojo, LocalDate date, String brand, String category) {
        SalesReportData data = new SalesReportData();
        data.setId(pojo.getId());
        data.setDate(date);
        data.setBrand(brand);
        data.setCategory(category);
        data.setQuantity(pojo.getQuantity());
        double revenue= pojo.getSellingPrice()*pojo.getQuantity();
        revenue=Math.round(revenue  * 100.0) / 100.0;
        data.setRevenue(revenue);
        return data;
    }

    public static OrderItemPojo convert(int productId,int quantity, double sellingPrice, int orderId) throws ApiException{
        OrderItemPojo orderItemPojo= new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setSellingPrice(((int)(sellingPrice  * 100)) / 100.0);
        return orderItemPojo;
    }
}
