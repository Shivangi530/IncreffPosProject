package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService service;

	@Test
	public void testAdd() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("adidas");
		p.setCategory("shoes");
		service.add(p);

		String expectedBrand= "adidas";
		String expectedCategory= "shoes";

		BrandPojo q= service.checkCombination(expectedBrand,expectedCategory);
		assertEquals(expectedBrand,q.getBrand());
		assertEquals(expectedCategory,q.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testBrandExistAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("Shoes");
		service.add(p);
	}
	@Test(expected = ApiException.class)
	public void testCategoryExistAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("dsfds");
		p.setCategory("");
		service.add(p);
	}
	@Test(expected = ApiException.class)
	public void testBrandCategoryExistAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("nestle");
		p.setCategory("dairy");
		service.add(p);

		BrandPojo q = new BrandPojo();
		q.setBrand("nestle");
		q.setCategory("dairy");
		service.add(q);
	}
	@Test
	public void testUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("Puma");
		p.setCategory("Sneakers");
		service.add(p);

		BrandPojo q=new BrandPojo();
		q.setBrand("adidas");
		q.setCategory("shoes");

		String expectedBrand= "adidas";
		String expectedCategory= "shoes";
		BrandPojo r= service.checkCombination(p.getBrand(), p.getCategory());

		service.update(r.getId(),q);

		BrandPojo s= service.get(r.getId());

		assertEquals(expectedBrand,s.getBrand());
		assertEquals(expectedCategory,s.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testBrandEmptyUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("Puma");
		p.setCategory("Sneakers");
		service.add(p);

		BrandPojo q=new BrandPojo();
		q.setBrand("");
		q.setCategory("Shoes");
		service.update(p.getId(),q);
	}
	@Test(expected = ApiException.class)
	public void testCategoryEmptyUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("Puma");
		p.setCategory("Sneakers");
		service.add(p);

		BrandPojo q=new BrandPojo();
		q.setBrand("Adidas");
		q.setCategory("");
		service.update(p.getId(),q);
	}
	@Test(expected = ApiException.class)
	public void testBrandCategoryExistUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("puma");
		p.setCategory("sneakers");
		service.add(p);

		BrandPojo q=new BrandPojo();
		q.setBrand("puma");
		q.setCategory("sneakers");

		BrandPojo r=new BrandPojo();
		r.setBrand("amul");
		r.setCategory("milk");
		service.add(r);

		service.update(r.getId(),q);
	}

	@Test
	public void testGetAll() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("puma");
		p.setCategory("sneakers");
		service.add(p);

		BrandPojo r=new BrandPojo();
		r.setBrand("amul");
		r.setCategory("milk");
		service.add(r);

		List<BrandPojo> a = service.getAll();
		assertEquals(2,a.size());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException{
		service.getValueBrandCategory(5);
		service.getCheck(5);
	}



}
