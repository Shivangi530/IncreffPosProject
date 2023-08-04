package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;

@Component
public class inventoryDto {
    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public InventoryData get(Integer id) throws ApiException {
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

    public void update(Integer id, InventoryForm f) throws ApiException {
        service.update(id, f.getQuantity());
    }

    public void updateList(List<InventoryForm> formList) throws ApiException {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();

        for (int i = 0; i < formList.size(); i++) {
            InventoryForm inventoryForm = formList.get(i);
            System.out.println("InventoryForm "+inventoryForm);
            Pair<Integer, String> errorPair;
            try {
                normalize(inventoryForm);
                validate(inventoryForm);
                System.out.println("InventoryForm "+inventoryForm);
                int id= productService.getIdByBarcode(inventoryForm.getBarcode());
                System.out.println("id= "+id);
                InventoryPojo inventoryPojo= convert(inventoryForm);
                inventoryPojo.setId(id);
                inventoryPojoList.add(inventoryPojo);
            } catch (ApiException e) {
                System.out.println("error: "+e);
                errorPair = new Pair<>(i , e.getMessage());
                errorList.add(errorPair);
            }
        }
        if(!errorList.isEmpty()){
            throw new ApiException(errorList.toString());
        }else{
            service.bulkUpdate(inventoryPojoList);
        }
    }


}