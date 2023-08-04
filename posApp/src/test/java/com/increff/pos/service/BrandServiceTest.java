package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

	@Test
	public void testAddNullBrand() {
		BrandPojo p = new BrandPojo();
		p.setCategory("Shoes");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand cannot be empty", e.getMessage());
		}
	}

	@Test
	public void testAddEmptyBrand() {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("Shoes");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand cannot be empty", e.getMessage());
		}
	}
	@Test
	public void testAddEmptyCategory() {
		BrandPojo p = new BrandPojo();
		p.setBrand("dsfds");
		p.setCategory("");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Category cannot be empty", e.getMessage());
		}
	}
	@Test
	public void testBrandCategoryExistAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("nestle");
		p.setCategory("dairy");
		service.add(p);

		BrandPojo q = new BrandPojo();
		q.setBrand("nestle");
		q.setCategory("dairy");
		try {
			service.add(q);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand:nestle and Category:dairy combination already exists", e.getMessage());
		}
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

		service.update(r.getId(),"adidas","shoes");

		BrandPojo s= service.getValueBrandCategory(r.getId());

		assertEquals(expectedBrand,s.getBrand());
		assertEquals(expectedCategory,s.getCategory());
	}

	@Test
	public void testBrandEmptyUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("Puma");
		p.setCategory("Sneakers");
		service.add(p);

		try {
			service.update(p.getId(),"","Shoes");
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand cannot be empty", e.getMessage());
		}
	}
	@Test
	public void testCategoryEmptyUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("Puma");
		p.setCategory("Sneakers");
		service.add(p);

		try {
			service.update(p.getId(),"Adidas","");
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Category cannot be empty", e.getMessage());
		}
	}
	@Test
	public void testBrandCategoryExistUpdate() throws ApiException{
		BrandPojo p=new BrandPojo();
		p.setBrand("puma");
		p.setCategory("sneakers");
		service.add(p);

		BrandPojo r=new BrandPojo();
		r.setBrand("amul");
		r.setCategory("milk");
		service.add(r);

		try {
			service.update(r.getId(),"puma","sneakers");
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand:puma and Category:sneakers combination already exists", e.getMessage());
		}
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

	@Test
	public void testGetCheck(){
		System.out.println("Hello world");
		try {
			service.getCheck(5);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Brand with given ID does not exit, id: 5", e.getMessage());
		}
	}

	@Test
	public void testBulkAdd() throws ApiException{
		BrandPojo p1=new BrandPojo();
		p1.setBrand("puma");
		p1.setCategory("shoes");
		service.add(p1);

		BrandPojo p=new BrandPojo();
		p.setBrand("puma");
		p.setCategory("sneakers");

		BrandPojo q=new BrandPojo();
		q.setBrand("adidas");
		q.setCategory("shoes");

		List<BrandPojo> list= new ArrayList<>();
		list.add(p);
		list.add(q);
		service.bulkAdd(list);

		List<BrandPojo> result = service.getAll();
		assertEquals(3,result.size());

		for (BrandPojo brand : result) {
			if (brand.getBrand().equals("puma") && brand.getCategory().equals("sneakers")) {
				assertTrue(true);
			} else if (brand.getBrand().equals("adidas") && brand.getCategory().equals("shoes")) {
				assertTrue(true);
			}else if (brand.getBrand().equals("puma") && brand.getCategory().equals("shoes")) {
				assertTrue(true);
			} else {
				// Unexpected brand or category found in the list
				fail("Unexpected brand or category found: " + brand.getBrand() + ", " + brand.getCategory());
			}
		}
	}



}
