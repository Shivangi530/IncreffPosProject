package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(dao.checkCombination(p.getBrand(),p.getCategory())!=null) {
			throw new ApiException("Brand and Category combination already exists");
		}
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(dao.checkCombination(p.getBrand(),p.getCategory())!=null) {
			if (id != checkCombination(p.getBrand(),p.getCategory()).getId()) {
				throw new ApiException("Brand and Category combination already exists");
			}
		}
		BrandPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}

	@Transactional
	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exit, id: " + id);
		}
		return p;
	}
	@Transactional
	public BrandPojo getValueBrandCategory(int id){
		return dao.select(id);
	}

	public BrandPojo checkCombination(String brand,String category) {
		return dao.checkCombination(brand,category);
	}
}