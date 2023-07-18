package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if (StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if (dao.checkBarcode(p.getBarcode()) != null) {
            throw new ApiException("Barcode already exists");
        }
        if (p.getMrp() <= 0) {
            throw new ApiException("Mrp should be positive");
        }
        if (p.getBrand_category() == 0) {
            throw new ApiException("Invalid Brand Category");
        }
        dao.insert(p);

    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public int getIdByBarcode(String barcode) throws ApiException {
        if(dao.checkBarcode(barcode)==null) {
            throw new ApiException("Barcode doesn't exist");
        }
        return dao.checkBarcode(barcode).getId();
    }
    @Transactional
    public double checkSellingPrice(double price,int productId) throws ApiException {
        if(price<=0) {
            throw new ApiException("Selling price should be positive. ");
        }
        double mrp= dao.checkId(productId).getMrp();
        if(price>mrp) {
            throw new ApiException("Selling price: "+price+" should be less than mrp: "+mrp);
        }
        return price;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {// TODO MAX LENGTH SET TO 255 characters for name, brand, category
        ProductPojo ex = get(id);
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if (StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if ((dao.checkBarcode(p.getBarcode()) != null)) {
            if (id != dao.checkBarcode(p.getBarcode()).getId()) {
                throw new ApiException("Barcode already exists");
            }
        }
        if (p.getMrp() <= 0) {
            throw new ApiException("Mrp should be positive");
        }

        ex.setBarcode(p.getBarcode());
        ex.setName(p.getName());
        ex.setMrp(p.getMrp());
        dao.update(ex);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }
    public ProductPojo checkId(int id) {
        return dao.checkId(id);
    }
    public String selectBarcode(int id) {
        return dao.selectBarcode(id);
    }


}
