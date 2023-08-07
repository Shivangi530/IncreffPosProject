package com.increff.pos.service;

import com.increff.pos.pojo.ProductPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProductServiceTest extends AbstractUnitTest {

	@Autowired
	private ProductService service;

	@Test
	public void testAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);

		ProductPojo q= service.get(p.getId());
		String expectedBarcode= "ndejf";
		Integer expectedBrandCategory= 1;
		double expectedMrp = 10.7;
		String expectedName= "Fanta 200 ml";
		assertEquals(expectedBarcode,q.getBarcode());
		assertEquals(expectedBrandCategory,q.getBrandCategory());
		assertEquals(expectedMrp,q.getMrp(),0.0001);
		assertEquals(expectedName,q.getName());
	}

	@Test
	public void testNameEmptyAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10);
		p.setName("");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Name cannot be empty", e.getMessage());
		}
	}

	@Test
	public void testBarcodeEmptyAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("");
		p.setBrandCategory(1);
		p.setMrp(10);
		p.setName("fdsfee");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Barcode cannot be empty", e.getMessage());
		}
	}

	@Test
	public void testMrpInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dsfds");
		p.setBrandCategory(1);
		p.setMrp(0);
		p.setName("fdsfee");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Mrp should be positive", e.getMessage());
		}
	}

	@Test
	public void testBarcodeExistsAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dasfds");
		p.setBrandCategory(1);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		try {
			service.add(q);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Barcode already exists", e.getMessage());
		}
	}

	@Test
	public void testBrandCategoryInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dsfds");
		p.setBrandCategory(0);
		p.setMrp(10);
		p.setName("fdsfee");
		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Invalid Brand Category", e.getMessage());
		}
	}

	@Test
	public void testUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(20.1);
		p.setName("Fanta 500 ml");
		service.update(q.getId(),"Fanta 500 ml","ndejf",20.1);

		ProductPojo r= service.get(q.getId());
		double expectedMrp = 20.1;
		String expectedName= "Fanta 500 ml";
		assertEquals(expectedMrp,r.getMrp(),0.0001);
		assertEquals(expectedName,r.getName());
	}

	@Test
	public void testNameEmptyUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		try {
			service.update(q.getId(),"",q.getBarcode(),q.getMrp());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Name cannot be empty", e.getMessage());
		}
	}

	@Test
	public void testBarcodeEmptyUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		try {
			service.update(q.getId(),q.getName(),"",q.getMrp());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Barcode cannot be empty", e.getMessage());
		}
	}

	@Test
	public void testMrpInvalidUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		try {
			service.update(q.getId(),q.getName(),q.getBarcode(),0);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Invalid Mrp", e.getMessage());
		}
	}

	@Test
	public void testBarcodeExistsUpdate() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dasfds");
		p.setBrandCategory(1);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfdsgfds");
		q.setBrandCategory(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		try {
			service.update(q.getId(),p.getName(),p.getBarcode(),p.getMrp());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Barcode already exists", e.getMessage());
		}
	}

	@Test
	public void testGetCheckId() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);

		ProductPojo q= service.checkId(p.getId());;
		String expectedBarcode= service.selectBarcode(p.getId());
		Integer expectedBrandCategory= 1;
		double expectedMrp = 10.7;
		String expectedName= "Fanta 200 ml";
		Integer expectedId=p.getId();
		Integer qId= service.getIdByBarcode("ndejf");
		assertEquals(expectedId,qId);
		assertEquals(expectedBarcode,q.getBarcode());
		assertEquals(expectedBrandCategory,q.getBrandCategory());
		assertEquals(expectedMrp,q.getMrp(),0.0001);
		assertEquals(expectedName,q.getName());
	}

	@Test
	public void testGetCheckIdInvalid() throws ApiException{
		try {
			service.getCheck(-1);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Product with given ID does not exit, id: -1", e.getMessage());
		}
	}

	@Test
	public void testGetAll() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrandCategory(10);
		q.setMrp(10.9);
		q.setName("fdsfee");

		List<ProductPojo> list=new ArrayList<>();
		list.add(p);
		list.add(q);
		service.bulkAdd(list);

		List<ProductPojo> r= service.getAll();
		assertEquals(2,r.size());
	}

	@Test
	public void testCheckSellingPrice() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=10.3;
		double price1= service.checkSellingPrice(price,p.getId());
		assertEquals(price1,price,0.00001);
	}

	@Test
	public void testCheckSellingPriceInvalidNegative() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=0;
		try {
			service.checkSellingPrice(price,p.getId());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Selling price should be positive. ", e.getMessage());
		}
	}

	@Test
	public void testCheckSellingPriceInvalid() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrandCategory(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=11;
		try {
			service.checkSellingPrice(price,p.getId());
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Selling price: 11.0 should be less than mrp: 10.7", e.getMessage());
		}
	}

	@Test
	public void testGetIdByBarcodeInvalid() throws ApiException{
		try {
			service.getIdByBarcode("gfdg");
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Barcode doesn't exist", e.getMessage());
		}
	}
}
