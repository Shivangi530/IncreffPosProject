package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import io.swagger.models.auth.In;
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
    public void add(InventoryPojo pojo) throws ApiException {
        if(pojo.getQuantity()<0){
            throw new ApiException("Quantity can not be negative");
        }
        dao.insert(pojo);
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
        if(quantity> Integer.MAX_VALUE){
            throw new ApiException("Quantity is too large.");
        }
        existingPojo.setQuantity(quantity);
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo pojo= dao.select(id);
        if (pojo == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
        return pojo;
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
        for (InventoryPojo pojo:list) {
            InventoryPojo currentPojo= getCheck(pojo.getId());
            update(pojo.getId(),pojo.getQuantity()+ currentPojo.getQuantity());
        }
    }
}
