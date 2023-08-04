package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

public class ValidateUtil {

    public static void validate(BrandForm p) throws ApiException {
        int brandLength=p.getBrand().length();
        int categoryLength=p.getCategory().length();
        if(brandLength==0){
            throw new ApiException("Brand cannot be empty");
        }
        if(categoryLength==0){
            throw new ApiException("Category cannot be empty");
        }
        if (brandLength>30){
            throw new ApiException("Brand length: "+ brandLength +" is more than 30 characters");
        }
        if (categoryLength>50){
            throw new ApiException("Category size: "+ brandLength +" is more than 50 characters");
        }
    }

    public static void validate(ProductForm p) throws ApiException {
        int brandLength=p.getBrand().length();
        int categoryLength=p.getCategory().length();
        int barcodeLength=p.getBarcode().length();
        int nameLength=p.getName().length();

        if (brandLength>30){
            throw new ApiException("Brand size: "+ brandLength +" is more than 30 characters");
        }
        if (categoryLength>50){
            throw new ApiException("Category size: "+ categoryLength +" is more than 50 characters");
        }
        if (barcodeLength>255){
            throw new ApiException("Barcode size: "+ barcodeLength +" is more than 255 characters");
        }
        if (nameLength>50){
            throw new ApiException("Name size: "+ nameLength +" is more than 50 characters");
        }
        if(p.getMrp()>1000000000){
            throw new ApiException("Mrp entered is too large");
        }
    }

    public static void validate(InventoryForm p) throws ApiException {
        if (p.getBarcode().length()>255){
            throw new ApiException("Barcode length: "+ p.getBarcode().length() +" is more than 255 characters");
        }
        if (p.getBarcode().length()==0){
            throw new ApiException("Barcode cannot be empty");
        }
        if(p.getQuantity() ==null){
            throw new ApiException("Quantity cannot be empty");
        }
        if(p.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
    }
}
