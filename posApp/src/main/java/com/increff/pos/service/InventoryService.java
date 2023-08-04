package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao dao;
	@Autowired
	private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {
        if(p.getQuantity()<0){
            throw new ApiException("Quantity can not be negative");
        }
        dao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, Integer quantity) throws ApiException {
        InventoryPojo existingPojo = getCheck(id);
        if(quantity<0){
            throw new ApiException("Quantity cannot be negative");
        }
        existingPojo.setQuantity(quantity);
        dao.update(existingPojo);
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo p= dao.select(id);
        if (p == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
        return p;
    }
    @Transactional
    public Integer checkQuantity(Integer quantity,Integer id) throws ApiException {
        getCheck(id);
        if(quantity<=0){
            throw new ApiException("Quantity should be positive");
        }
        if(dao.select(id).getQuantity()<quantity) {
            throw new ApiException("Selected quantity:"+quantity+" is more than inventory: "+dao.select(id).getQuantity()+" for barcode:"+productService.get(id).getBarcode());
        }return quantity;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void bulkUpdate(List<InventoryPojo> list) throws ApiException {
        for (InventoryPojo p:list) {
            InventoryPojo currentPojo= getCheck(p.getId());
            update(p.getId(),p.getQuantity()+ currentPojo.getQuantity());
        }
    }
}
