package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
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
    public InventoryPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, InventoryPojo p) throws ApiException {
        InventoryPojo existingPojo = getCheck(id);
        if(p.getQuantity()<0){
            throw new ApiException("Quantity can not be negative");
        }
        existingPojo.setQuantity(p.getQuantity());
        dao.update(existingPojo);
    }

    @Transactional
    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo p= dao.select(id);
        if (p == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
        return p;
    }
    @Transactional
    public int checkQuantity(int quantity,int id) throws ApiException {
        if(quantity<=0){
            throw new ApiException("Quantity should be positive");
        }
        if(dao.select(id).getQuantity()<quantity) {
            throw new ApiException("Selected quantity:"+quantity+" is more than that of inventory: "+dao.select(id).getQuantity()+" for barcode:"+productService.get(id).getBarcode());
        }return quantity;
    }
}
