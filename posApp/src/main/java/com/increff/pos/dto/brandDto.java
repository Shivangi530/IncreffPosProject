package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.BrandService;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;

@Component
public class brandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        BrandPojo p = convert(form);// todo: to use copy bean and create a generic function
        brandService.add(p);
    }
    public BrandData getBrand(int id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
        return convert(brandPojo);
    }

    // todo: implement pagination
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
