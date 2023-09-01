package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo pojo) throws ApiException {
        checkAll(pojo);
        dao.insert(pojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public Integer getIdByBarcode(String barcode) throws ApiException {
        if(dao.checkBarcode(barcode)==null) {
            throw new ApiException("Barcode doesn't exist");
        }
        return dao.checkBarcode(barcode).getId();
    }
    @Transactional
    public double checkSellingPrice(double price,Integer productId) throws ApiException {
        if(price<=0) {
            throw new ApiException("Selling price should be positive. ");
        }
        if(price>Double.MAX_VALUE) {
            throw new ApiException("Selling price is too large. ");
        }
        double mrp= dao.checkId(productId).getMrp();
        if(price>mrp) {
            throw new ApiException("Selling price: "+price+" should be less than mrp: "+mrp);
        }
        return price;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, String name, String barcode, double mrp) throws ApiException {
        ProductPojo existingPojo = get(id);
        if (StringUtil.isEmpty(name)) {
            throw new ApiException("Name cannot be empty");
        }
        if (StringUtil.isEmpty(barcode)) {
            throw new ApiException("Barcode cannot be empty");
        }
        if ((dao.checkBarcode(barcode) != null)) {
            if (id != dao.checkBarcode(barcode).getId()) {
                throw new ApiException("Barcode already exists");
            }
        }
        if (mrp <= 0) {
            throw new ApiException("Invalid Mrp");
        }
        existingPojo.setName(name);
        existingPojo.setMrp(mrp);
        dao.update(existingPojo);
    }

    @Transactional
    public ProductPojo getCheck(Integer id) throws ApiException {
        ProductPojo pojo = dao.select(id);
        if (pojo == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return pojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<ProductPojo> list) throws ApiException {
        for (ProductPojo pojo:list) {
            add(pojo);
        }
    }

    public void checkAll(ProductPojo pojo) throws ApiException{
        if (StringUtil.isEmpty(pojo.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if (StringUtil.isEmpty(pojo.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if (dao.checkBarcode(pojo.getBarcode()) != null) {
            throw new ApiException("Barcode already exists");
        }
        if (pojo.getMrp() <= 0) {
            throw new ApiException("Mrp should be positive");
        }
        if (pojo.getBrandCategory() == 0) {
            throw new ApiException("Invalid Brand Category");
        }
    }

    public void checkAll(String name, String barcode, double mrp, int brand_category) throws ApiException{
        if (StringUtil.isEmpty(name)) {
            throw new ApiException("Name cannot be empty");
        }
        if (StringUtil.isEmpty(barcode)) {
            throw new ApiException("Barcode cannot be empty");
        }
        if (dao.checkBarcode(barcode) != null) {
            throw new ApiException("Barcode already exists");
        }
        if (mrp <= 0) {
            throw new ApiException("Invalid mrp");
        }
        if (brand_category == 0) {
            throw new ApiException("Invalid Brand Category");
        }
    }

    public ProductPojo checkId(Integer id) {
        return dao.checkId(id);
    }
    public String selectBarcode(Integer id) {
        return dao.selectBarcode(id);
    }
}
