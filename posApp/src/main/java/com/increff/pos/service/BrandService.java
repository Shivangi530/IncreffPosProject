package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

//TODO: transactional to class level
@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo p) throws ApiException {
		checkAll(p.getBrand(),p.getCategory());
		dao.insert(p);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}


	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, String brand, String category) throws ApiException { //todo: don't use pojo for updating
		checkAll(brand,category);
		BrandPojo existingPojo = getCheck(id);
		existingPojo.setCategory(category);
		existingPojo.setBrand(brand);
		dao.update(existingPojo);
	}

	@Transactional
	public BrandPojo getCheck(Integer id) throws ApiException {
		BrandPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exit, id: " + id);
		}
		return p;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void bulkAdd(List<BrandPojo> list) throws ApiException {
		for (BrandPojo p:list) {
			add(p);
		}
	}

	public BrandPojo getValueBrandCategory(Integer id){
		return dao.select(id);
	}

	public BrandPojo checkCombination(String brand,String category) {
		return dao.checkCombination(brand,category);
	}

	public void checkAll(String brand,String category) throws ApiException{
		if(StringUtil.isEmpty(brand)) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(category)) {
			throw new ApiException("Category cannot be empty");
		}
		if(dao.checkCombination(brand,category)!=null) {
			throw new ApiException("Brand:"+brand+" and Category:"+category+" combination already exists");
		}
	}
}

