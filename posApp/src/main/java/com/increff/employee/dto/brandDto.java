package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.service.BrandService;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

import static com.increff.employee.util.ConversionUtil.convert;
import static com.increff.employee.util.NormaliseUtil.normalize;
import static com.increff.employee.util.ValidateUtil.validate;

@Component
public class brandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        BrandPojo p = convert(form);
        brandService.add(p);
    }
    public BrandData getBrand(int id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id, BrandForm f) throws ApiException {
        normalize(f);
        validate(f);
        BrandPojo p = convert(f);
        brandService.update(id, p);
    }
}
