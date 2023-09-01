package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;

import java.util.Objects;

public class ValidateUtil {

    public static void validate(BrandForm form) throws ApiException {
        int brandLength=form.getBrand().length();
        int categoryLength=form.getCategory().length();
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
            throw new ApiException("Category size: "+ categoryLength +" is more than 50 characters");
        }
    }

    public static void validate(ProductForm form) throws ApiException {
        int brandLength=form.getBrand().length();
        int categoryLength=form.getCategory().length();
        int barcodeLength=form.getBarcode().length();
        int nameLength=form.getName().length();

        if (brandLength>30){
            throw new ApiException("Brand size: "+ brandLength +" is more than 30 characters");
        }
        if (categoryLength>50){
            throw new ApiException("Category size: "+ categoryLength +" is more than 50 characters");
        }
        if (barcodeLength>50){
            throw new ApiException("Barcode size: "+ barcodeLength +" is more than 50 characters");
        }
        if (nameLength>50){
            throw new ApiException("Name size: "+ nameLength +" is more than 50 characters");
        }
        if(form.getMrp()>10000000){
            throw new ApiException("Mrp entered is too large");
        }
    }

    public static void validate(InventoryForm form) throws ApiException {
        if (form.getBarcode().length()>50){
            throw new ApiException("Barcode length: "+ form.getBarcode().length() +" is more than 50 characters");
        }
        if (form.getBarcode().length()==0){
            throw new ApiException("Barcode cannot be empty");
        }
        if(form.getQuantity() ==null){
            throw new ApiException("Quantity cannot be empty");
        }
        if(form.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
        if(form.getQuantity() %1 !=0){
            throw new ApiException("Quantity must be an integer");
        }
        if(form.getQuantity()>10000000){
            throw new ApiException("Quantity entered is too large");
        }
    }

    public static void validate(ProductTsvForm form) throws ApiException {
        int brandLength=form.getBrand().length();
        int categoryLength=form.getCategory().length();
        int barcodeLength=form.getBarcode().length();
        int nameLength=form.getName().length();
        int mrpLength=form.getMrp().length();
        int decimalIndex = form.getMrp().indexOf('.');

        if (decimalIndex != -1) {
            String substring = form.getMrp().substring(0, decimalIndex);
            if(substring.length()>7)
                throw new ApiException("Mrp entered is too large");
        }else if(mrpLength>7){
            throw new ApiException("Mrp entered is too large");
        }
        if (brandLength>30){
            throw new ApiException("Brand size: "+ brandLength +" is more than 30 characters");
        }
        if (categoryLength>50){
            throw new ApiException("Category size: "+ categoryLength +" is more than 50 characters");
        }
        if (barcodeLength>50){
            throw new ApiException("Barcode size: "+ barcodeLength +" is more than 50 characters");
        }
        if (nameLength>50){
            throw new ApiException("Name size: "+ nameLength +" is more than 50 characters");
        }
        if (form.getMrp().isEmpty() || Objects.isNull(form.getMrp())){
            throw new ApiException("Mrp cannot be empty");
        }
        Double mrp=0.0;
        try {
            mrp= Double.parseDouble(form.getMrp());
        } catch (NumberFormatException e) {
            throw new ApiException("Mrp must be an integer");
        }
        if(mrp>=10000000){
            throw new ApiException("Mrp entered is too large");
        }
    }

    public static void validate(InventoryTsvForm form) throws ApiException {
        if (form.getBarcode().length()>50){
            throw new ApiException("Barcode length: "+ form.getBarcode().length() +" is more than 50 characters");
        }
        if (form.getBarcode()==null || form.getBarcode().length()==0){
            throw new ApiException("Barcode cannot be empty");
        }
        if(form.getQuantity() ==null || form.getQuantity().length()==0){
            throw new ApiException("Quantity cannot be empty");
        }
        Integer quantity=0;
        try {
            quantity= Integer.parseInt(form.getQuantity());
        } catch (NumberFormatException e) {
            throw new ApiException("Quantity must be an integer");
        }
        if(quantity<0){
            throw new ApiException("Quantity cannot be negative");
        }
        if(quantity>=10000000){
            throw new ApiException("Quantity entered is too large");
        }
    }
}
