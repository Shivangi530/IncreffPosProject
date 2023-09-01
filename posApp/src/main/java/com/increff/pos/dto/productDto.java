package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductTsvForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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

    public void add(ProductForm form) throws ApiException {
        if (Objects.isNull(form.getBrand()) || Objects.isNull(form.getCategory())|| Objects.isNull(form.getBarcode())|| Objects.isNull(form.getName())|| Objects.isNull(form.getMrp())) {
            throw new ApiException("All fields must be non-null strings.");
        }
        normalize(form);
        validate(form);
        Integer brandCategoryId = 0;
        if (brandService.checkCombination(form.getBrand(), form.getCategory()) != null) {
            brandCategoryId = brandService.checkCombination(form.getBrand(), form.getCategory()).getId();
        }
        ProductPojo p = convert(form, brandCategoryId);
        service.add(p);
        InventoryPojo pi = new InventoryPojo();
        pi.setId(p.getId());
        pi.setQuantity(0);
        inventoryService.add(pi);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = service.get(id);
        String brand = brandService.getCheck(p.getBrandCategory()).getBrand();
        String category = brandService.getCheck(p.getBrandCategory()).getCategory();
        return convert(p, brand, category);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for (ProductPojo p : list) {
            BrandPojo brandPojo= brandService.getValueBrandCategory(p.getBrandCategory());
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            list2.add(convert(p, brand, category));
        }
        return list2;
    }

    public void update(Integer id, ProductForm form) throws ApiException {
        normalize(form);
        validate(form);

        Integer brandCategory = 0;
        if (brandService.checkCombination(form.getBrand(), form.getCategory()) != null) {
            brandCategory = brandService.checkCombination(form.getBrand(), form.getCategory()).getId();
        }
        ProductPojo p = convert(form, brandCategory);

        service.update(id, p.getName(),p.getBarcode(),p.getMrp());
    }

    public void addList(List<ProductTsvForm> formList) throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();
        Set<String> checkDuplicateBarcode = new HashSet<>();
        for (int i = 0; i < formList.size(); i++) {
            ProductTsvForm productForm = formList.get(i);
            Pair<Integer, String> errorPair;
            try {
                normalize(productForm);
                validate(productForm);
                BrandPojo brandPojo = brandService.checkCombination(productForm.getBrand(), productForm.getCategory());
                int brandCategoryId=0;
                if (brandPojo != null) {
                    brandCategoryId = brandPojo.getId();
                }
                double mrp=0.0;
                mrp= Double.parseDouble(productForm.getMrp());
                mrp= ((int)(mrp * 100) / 100.0);
                ProductForm form= convert(productForm,mrp);
                service.checkAll(productForm.getName(),productForm.getBarcode(),mrp,brandCategoryId);
                if (!checkDuplicateBarcode.add(productForm.getBarcode())) {
                    throw new ApiException("Duplicate entry for Barcode: " + productForm.getBarcode() );
                }
                if (Objects.isNull(form.getBrand()) || Objects.isNull(form.getCategory())|| Objects.isNull(form.getBarcode())|| Objects.isNull(form.getName())|| Objects.isNull(form.getMrp())) {
                    throw new ApiException("All fields must be non-null strings.");
                }
                productFormList.add(form);
            } catch (ApiException e) {
                errorPair = new Pair<>(i , e.getMessage());
                errorList.add(errorPair);
            }
        }
        if(!errorList.isEmpty()){
            throw new ApiException(errorList.toString());
        }else{
            bulkAdd(productFormList);
        }
    }

    public void bulkAdd(List<ProductForm> productFormList) throws ApiException{
        for (ProductForm form :productFormList) {
            add(form);
        }
    }

}