package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void add(ProductForm f) throws ApiException {
        normalize(f);
        validate(f);
        Integer brandCategoryId = 0;
        if (brandService.checkCombination(f.getBrand(), f.getCategory()) != null) {
            brandCategoryId = brandService.checkCombination(f.getBrand(), f.getCategory()).getId();
        }
        ProductPojo p = convert(f, brandCategoryId);
        service.add(p);
        InventoryPojo pi = new InventoryPojo(); // todo: to create the inventory pojo in the service layer
        pi.setId(p.getId());
        pi.setQuantity(0);
        inventoryService.add(pi);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = service.get(id);
        String brand = brandService.getCheck(p.getBrand_category()).getBrand();
        String category = brandService.getCheck(p.getBrand_category()).getCategory();
        return convert(p, brand, category);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for (ProductPojo p : list) {
            BrandPojo brandPojo= brandService.getValueBrandCategory(p.getBrand_category());
            String brand = brandPojo.getBrand(); //todo: to call once by using the list api
            String category = brandPojo.getCategory();
            list2.add(convert(p, brand, category));
        }
        return list2;
    }

    public void update(Integer id, ProductForm f) throws ApiException {
        normalize(f);
        validate(f);

        Integer brandCategory = 0;
        if (brandService.checkCombination(f.getBrand(), f.getCategory()) != null) {
            brandCategory = brandService.checkCombination(f.getBrand(), f.getCategory()).getId();
        }
        System.out.println(brandCategory);
        ProductPojo p = convert(f, brandCategory);

        service.update(id, p.getName(),p.getBarcode(),p.getMrp());
    }

    public void addList(List<ProductForm> formList) throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();
        Set<String> checkDuplicateBarcode = new HashSet<>();
        for (int i = 0; i < formList.size(); i++) {
            ProductForm productForm = formList.get(i);
            Pair<Integer, String> errorPair;
            try {
                normalize(productForm);
                validate(productForm);
                Integer brandCategoryId = brandService.checkCombination(productForm.getBrand(), productForm.getCategory()).getId();
                if (brandCategoryId == null) {
                    brandCategoryId = 0;
                }
                ProductPojo productPojo= convert(productForm,brandCategoryId);
                service.checkAll(productPojo);
                if (!checkDuplicateBarcode.add(productPojo.getBarcode())) {
                    throw new ApiException("Duplicate entry for Barcode: " + productPojo.getBarcode() );
                }
                productPojoList.add(productPojo);
            } catch (ApiException e) {
                errorPair = new Pair<>(i , e.getMessage());
                errorList.add(errorPair);
            }
        }
        System.out.println(errorList.size());
        for (Pair<Integer, String> p : errorList) {
            System.out.println(p.getKey()+"....."+p.getValue());
        }
        System.out.println(checkDuplicateBarcode.size());
        if(!errorList.isEmpty()){
            throw new ApiException(errorList.toString());
        }else{
            service.bulkAdd(productPojoList);
        }
    }



}