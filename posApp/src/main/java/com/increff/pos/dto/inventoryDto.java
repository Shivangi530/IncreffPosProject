package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

@Component
public class inventoryDto {
    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public InventoryData get(int id) throws ApiException {
        InventoryPojo p = service.get(id);
        String barcode=productService.selectBarcode(p.getId());
        return convert(p,barcode);
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> list = service.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo p : list) {
            String barcode=productService.selectBarcode(p.getId());
            list2.add(convert(p,barcode));
        }
        return list2;
    }

    public void update(int id, InventoryForm f) throws ApiException {
        InventoryPojo p = convert(f);
        service.update(id, p);
    }
}