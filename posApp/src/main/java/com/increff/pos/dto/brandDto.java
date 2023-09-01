package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.BrandService;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;

@Component
public class brandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm form) throws ApiException {
        if (Objects.isNull(form.getBrand()) || Objects.isNull(form.getCategory())) {
            throw new ApiException("Brand and category must be non-null strings.");
        }
        normalize(form);
        validate(form);
        BrandPojo pojo = convert(form);
        brandService.add(pojo);
    }
    public BrandData getBrand(Integer id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> pojoList = brandService.getAll();
        List<BrandData> dataList = new ArrayList<>();
        for (BrandPojo pojo : pojoList) {
            dataList.add(convert(pojo));
        }
        return dataList;
    }

    public void update(Integer id, BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        brandService.update(id, form.getBrand(),form.getCategory());
    }

    public void addList(List<BrandForm> formList) throws ApiException {
        List<BrandPojo> brandPojoList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();
        Set<String> checkDuplicate = new HashSet<>();
        for (int i = 0; i < formList.size(); i++) {
            BrandForm brandForm = formList.get(i);
            Pair<Integer, String> errorPair;
            try {
                normalize(brandForm);
                validate(brandForm);
                brandService.checkAll(brandForm.getBrand(),brandForm.getCategory());
                BrandPojo brandPojo= convert(brandForm);
                String brandCategoryKey = brandPojo.getBrand() + "#" + brandPojo.getCategory();
                if (!checkDuplicate.add(brandCategoryKey)) {
                    throw new ApiException("Duplicate entry for Brand: " + brandPojo.getBrand() + " Category: " + brandPojo.getCategory());
                }
                brandPojoList.add(brandPojo);
            } catch (ApiException e) {
                errorPair = new Pair<>(i , e.getMessage());
                errorList.add(errorPair);
            }
        }
        if(!errorList.isEmpty()){
            throw new ApiException(errorList.toString());
        }else{
            brandService.bulkAdd(brandPojoList);
        }
    }
}
