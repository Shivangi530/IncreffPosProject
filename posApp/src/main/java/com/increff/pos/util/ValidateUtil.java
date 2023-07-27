package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

public class ValidateUtil {

    public static void validate(BrandForm p) throws ApiException {
        int brandLength=p.getBrand().length();
        int categoryLength=p.getCategory().length();
        if (brandLength>255){
            throw new ApiException("Brand length: "+ brandLength +" is more than 255 characters");
        }
        if (categoryLength>255){
            throw new ApiException("Category size: "+ brandLength +" is more than 255 characters");
        }
    }

    public static void validate(ProductForm p) throws ApiException {
        int brandLength=p.getBrand().length();
        int categoryLength=p.getCategory().length();
        int barcodeLength=p.getBarcode().length();
        int nameLength=p.getName().length();

        if (brandLength>255){
            throw new ApiException("Brand size: "+ brandLength +" is more than 255 characters");
        }
        if (categoryLength>255){
            throw new ApiException("Category size: "+ categoryLength +" is more than 255 characters");
        }
        if (barcodeLength>255){
            throw new ApiException("Barcode size: "+ barcodeLength +" is more than 255 characters");
        }
        if (nameLength>255){
            throw new ApiException("Name size: "+ nameLength +" is more than 255 characters");
        }
        if(p.getMrp()>1000000000){
            throw new ApiException("Mrp entered is too large");
        }
    }
}
