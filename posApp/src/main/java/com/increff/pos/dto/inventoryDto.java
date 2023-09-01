package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryTsvForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;
import static java.lang.Integer.parseInt;

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
        List<InventoryData> list2 = new ArrayList<>();
        for (InventoryPojo p : list) {
            String barcode=productService.selectBarcode(p.getId());
            list2.add(convert(p,barcode));
        }
        return list2;
    }

    public void update(Integer id, InventoryForm inventoryForm) throws ApiException {
        normalize(inventoryForm);
        if(inventoryForm.getQuantity() == null || inventoryForm.getQuantity() %1 !=0 || inventoryForm.getQuantity()>10000000){
            throw new ApiException("Invalid quantity");
        }
        Integer quantityInt = inventoryForm.getQuantity().intValue();
        service.update(id, quantityInt);
    }

    public void updateList(List<InventoryTsvForm> formList) throws ApiException {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();

        for (int i = 0; i < formList.size(); i++) {
            InventoryTsvForm inventoryForm = formList.get(i);
            Pair<Integer, String> errorPair;
            try {
                normalize(inventoryForm);
                validate(inventoryForm);
                Integer id= productService.getIdByBarcode(inventoryForm.getBarcode());
                InventoryPojo inventoryPojo= new InventoryPojo();
                inventoryPojo.setQuantity(parseInt(inventoryForm.getQuantity()));
                inventoryPojo.setId(id);
                inventoryPojoList.add(inventoryPojo);
            } catch (ApiException e) {
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