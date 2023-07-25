package com.increff.pos.service;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {

	@Autowired
	private InventoryService service;
	@Autowired
	private ProductService productService;

	@Test
	public void testAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer expectedInventory= 10;

		InventoryPojo r= service.get(p.getId());
		assertEquals(expectedInventory,q.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testQuantityInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(-1);
		q.setId(p.getId());
		service.add(q);
	}

	@Test
	public void testGetAll() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		ProductPojo p1=new ProductPojo();
		p1.setBarcode("ndejfadsfsa");
		p1.setBrand_category(12);
		p1.setMrp(10.72);
		p1.setName("Fanta 22200 ml");
		productService.add(p1);

		InventoryPojo q1=new InventoryPojo();
		q1.setQuantity(190);
		q1.setId(p1.getId());
		service.add(q1);

		List<InventoryPojo>	l= service.getAll();
		assertEquals(2,l.size());
	}

	@Test
	public void testUpdate() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		InventoryPojo q1= service.get(p.getId());
		q1.setQuantity(120);
		service.update(p.getId(),q1);
		Integer expectedInventory= 120;

		InventoryPojo r= service.get(p.getId());
		assertEquals(expectedInventory,r.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testUpdateQuantity() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		InventoryPojo q1= service.get(p.getId());
		q1.setQuantity(-1);
		service.update(p.getId(),q1);
	}

	@Test
	public void testCheckQuantity() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer quantity=8;
		Integer quantity1= service.checkQuantity(quantity,p.getId());

		assertEquals(quantity1,quantity);
	}

	@Test(expected = ApiException.class)
	public void testCheckQuantityNegative() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer quantity=-1;
		Integer quantity1= service.checkQuantity(quantity,p.getId());
	}

	@Test(expected = ApiException.class)
	public void testCheckQuantityLess() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer quantity=11;
		Integer quantity1= service.checkQuantity(quantity,p.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetCheckInvalid() throws ApiException{
		service.getCheck(-1);
	}
//	@Test(expected = ApiException.class)
//	public void testCategoryExistAdd() throws ApiException {
//		InventoryPojo p = new InventoryPojo();
//		p.setInventory("dsfds");
//		p.setCategory("");
//		service.add(p);
//	}
//	@Test(expected = ApiException.class)
//	public void testInventoryCategoryExistAdd() throws ApiException {
//		InventoryPojo p = new InventoryPojo();
//		p.setInventory("nestle");
//		p.setCategory("dairy");
//		service.add(p);
//
//		InventoryPojo q = new InventoryPojo();
//		q.setInventory("nestle");
//		q.setCategory("dairy");
//		service.add(q);
//	}
//	@Test
//	public void testUpdate() throws ApiException{
//		InventoryPojo p=new InventoryPojo();
//		p.setInventory("Puma");
//		p.setCategory("Sneakers");
//		service.add(p);
//
//		InventoryPojo q=new InventoryPojo();
//		q.setInventory("adidas");
//		q.setCategory("shoes");
//
//		String expectedInventory= "adidas";
//		String expectedCategory= "shoes";
//		InventoryPojo r= service.checkCombination(p.getInventory(), p.getCategory());
//
//		service.update(r.getId(),q);
//
//		InventoryPojo s= service.get(r.getId());
//
//		assertEquals(expectedInventory,s.getInventory());
//		assertEquals(expectedCategory,s.getCategory());
//	}
//
//	@Test(expected = ApiException.class)
//	public void testInventoryEmptyUpdate() throws ApiException{
//		InventoryPojo p=new InventoryPojo();
//		p.setInventory("Puma");
//		p.setCategory("Sneakers");
//		service.add(p);
//
//		InventoryPojo q=new InventoryPojo();
//		q.setInventory("");
//		q.setCategory("Shoes");
//		service.update(p.getId(),q);
//	}
//	@Test(expected = ApiException.class)
//	public void testCategoryEmptyUpdate() throws ApiException{
//		InventoryPojo p=new InventoryPojo();
//		p.setInventory("Puma");
//		p.setCategory("Sneakers");
//		service.add(p);
//
//		InventoryPojo q=new InventoryPojo();
//		q.setInventory("Adidas");
//		q.setCategory("");
//		service.update(p.getId(),q);
//	}
//	@Test(expected = ApiException.class)
//	public void testInventoryCategoryExistUpdate() throws ApiException{
//		InventoryPojo p=new InventoryPojo();
//		p.setInventory("puma");
//		p.setCategory("sneakers");
//		service.add(p);
//
//		InventoryPojo q=new InventoryPojo();
//		q.setInventory("puma");
//		q.setCategory("sneakers");
//
//		InventoryPojo r=new InventoryPojo();
//		r.setInventory("amul");
//		r.setCategory("milk");
//		service.add(r);
//
//		service.update(r.getId(),q);
//	}
//
//	@Test
//	public void testGetAll() throws ApiException{
//		InventoryPojo p=new InventoryPojo();
//		p.setInventory("puma");
//		p.setCategory("sneakers");
//		service.add(p);
//
//		InventoryPojo r=new InventoryPojo();
//		r.setInventory("amul");
//		r.setCategory("milk");
//		service.add(r);
//
//		List<InventoryPojo> a = service.getAll();
//		assertEquals(2,a.size());
//	}
//
//	@Test(expected = ApiException.class)
//	public void testGetCheck() throws ApiException{
//		service.getValueInventoryCategory(5);
//		service.getCheck(5);
//	}
//


}
