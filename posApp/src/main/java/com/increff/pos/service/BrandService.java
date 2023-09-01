package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo pojo) throws ApiException {
		checkAll(pojo.getBrand(),pojo.getCategory());
		dao.insert(pojo);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}


	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, String brand, String category) throws ApiException {
		if(StringUtil.isEmpty(brand)) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(category)) {
			throw new ApiException("Category cannot be empty");
		}
		if (dao.checkCombination(brand,category)!=null) {
			if (id != dao.checkCombination(brand,category).getId()) {
				throw new ApiException("Brand:"+brand+" and Category:"+category+" combination already exists");
			}
		}
		BrandPojo existingPojo = getCheck(id);
		existingPojo.setCategory(category);
		existingPojo.setBrand(brand);
	}

	@Transactional
	public BrandPojo getCheck(Integer id) throws ApiException {
		BrandPojo pojo = dao.select(id);
		if (pojo == null) {
			throw new ApiException("Brand with given ID does not exit, id: " + id);
		}
		return pojo;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void bulkAdd(List<BrandPojo> list) throws ApiException {
		for (BrandPojo pojo:list) {
			add(pojo);
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

