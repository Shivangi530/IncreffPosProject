package com.increff.pos.service;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InventoryServiceTest extends AbstractUnitTest {

	@Autowired
	private InventoryService service;
	@Autowired
	private ProductService productService;

	@Test
	public void testAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
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

	@Test
	public void testQuantityInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(-1);
		q.setId(p.getId());
		try {
			service.add(q);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Quantity can not be negative", e.getMessage());
		}
	}

	@Test
	public void testGetAll() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		ProductPojo p1=new ProductPojo();
		p1.setBarcode("ndejfadsfsa");
		p1.setBrandCategory(12);
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
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		InventoryPojo q1= service.get(p.getId());
		q1.setQuantity(120);
		service.update(p.getId(),120);
		Integer expectedInventory= 120;

		InventoryPojo r= service.get(p.getId());
		assertEquals(expectedInventory,r.getQuantity());
	}

	@Test
	public void testUpdateQuantity() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		try {
			service.update(p.getId(),-1);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Quantity cannot be negative", e.getMessage());
		}
	}

	@Test
	public void testCheckQuantity() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
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

	@Test
	public void testCheckQuantityNegative() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer quantity=-1;
		try {
			service.checkQuantity(quantity,p.getId());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Quantity should be positive", e.getMessage());
		}
	}

	@Test
	public void testCheckQuantityLess() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		productService.add(p);

		InventoryPojo q=new InventoryPojo();
		q.setQuantity(10);
		q.setId(p.getId());
		service.add(q);

		Integer quantity=11;
		try {
			service.checkQuantity(quantity,p.getId());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Selected quantity:11 is more than inventory: 10 for barcode:ndejf", e.getMessage());
		}
	}

	@Test
	public void testGetCheckInvalid() throws ApiException{
		try {
			service.getCheck(-1);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Inventory with given ID does not exit, id: -1", e.getMessage());
		}
	}

}
