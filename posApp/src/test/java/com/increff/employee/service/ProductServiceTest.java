package com.increff.employee.service;

import com.increff.employee.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {

	@Autowired
	private ProductService service;

	@Test
	public void testAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);

		ProductPojo q= service.get(p.getId());
		String expectedBarcode= "ndejf";
		int expectedBrand_Category= 1;
		double expectedMrp = 10.7;
		String expectedName= "Fanta 200 ml";
		assertEquals(expectedBarcode,q.getBarcode());
		assertEquals(expectedBrand_Category,q.getBrand_category());
		assertEquals(expectedMrp,q.getMrp(),0.0001);
		assertEquals(expectedName,q.getName());
	}

	@Test(expected = ApiException.class)
	public void testNameEmptyAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10);
		p.setName("");
		service.add(p);
	}

	@Test(expected = ApiException.class)
	public void testBarcodeEmptyAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("");
		p.setBrand_category(1);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);
	}

	@Test(expected = ApiException.class)
	public void testMrpInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dsfds");
		p.setBrand_category(1);
		p.setMrp(0);
		p.setName("fdsfee");
		service.add(p);
	}

	@Test(expected = ApiException.class)
	public void testBarcodeExistsAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dasfds");
		p.setBrand_category(1);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);
	}

	@Test(expected = ApiException.class)
	public void testBrandCategoryInvalidAdd() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dsfds");
		p.setBrand_category(0);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);
	}

	@Test
	public void testUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(20.1);
		p.setName("Fanta 500 ml");
		service.update(q.getId(),p);

		ProductPojo r= service.get(q.getId());
		String expectedBarcode= "ndejf";
		double expectedMrp = 20.1;
		String expectedName= "Fanta 500 ml";
		assertEquals(expectedBarcode,r.getBarcode());
		assertEquals(expectedMrp,r.getMrp(),0.0001);
		assertEquals(expectedName,r.getName());
	}

	@Test(expected = ApiException.class)
	public void testNameEmptyUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo p= service.get(q.getId());
		p.setName("");
		service.update(q.getId(),p);
	}

	@Test(expected = ApiException.class)
	public void testBarcodeEmptyUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo p= service.get(q.getId());
		p.setBarcode("");
		service.update(q.getId(),p);
	}

	@Test(expected = ApiException.class)
	public void testMrpInvalidUpdate() throws ApiException{
		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo p= service.get(q.getId());
		p.setMrp(0);
		service.update(q.getId(),p);
	}

	@Test(expected = ApiException.class)
	public void testBarcodeExistsUpdate() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("dasfds");
		p.setBrand_category(1);
		p.setMrp(10);
		p.setName("fdsfee");
		service.add(p);

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfddfsas");
		q.setBrand_category(10);
		q.setMrp(10);
		q.setName("fdsfee");
		service.add(q);

		ProductPojo r= service.get(q.getId());
		r.setBarcode("dasfds");
		service.update(q.getId(),p);
	}

	@Test
	public void testGetCheckId() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);

		ProductPojo q= service.checkId(p.getId());;
		String expectedBarcode= service.selectBarcode(p.getId());
		int expectedBrand_Category= 1;
		double expectedMrp = 10.7;
		String expectedName= "Fanta 200 ml";
		assertEquals(q.getId(),service.getIdByBarcode("ndejf"));
		assertEquals(expectedBarcode,q.getBarcode());
		assertEquals(expectedBrand_Category,q.getBrand_category());
		assertEquals(expectedMrp,q.getMrp(),0.0001);
		assertEquals(expectedName,q.getName());
	}

	@Test(expected = ApiException.class)
	public void testGetCheckIdInvalid() throws ApiException{
		ProductPojo p= service.getCheck(-1);
	}

	@Test
	public void testGetAll() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);

		ProductPojo q=new ProductPojo();
		q.setBarcode("dasfds");
		q.setBrand_category(10);
		q.setMrp(10.9);
		q.setName("fdsfee");
		service.add(q);

		List<ProductPojo> r= service.getAll();
		assertEquals(2,r.size());
	}

	@Test
	public void testCheckSellingPrice() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=10.3;
		double price1= service.checkSellingPrice(price,p.getId());
		assertEquals(price1,price,0.00001);
	}

	@Test(expected = ApiException.class)
	public void testCheckSellingPriceInvalidNegative() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=0;
		double price1= service.checkSellingPrice(price,p.getId());
		assertEquals(price1,price,0.00001);
	}

	@Test(expected = ApiException.class)
	public void testCheckSellingPriceInvalid() throws ApiException{
		ProductPojo p=new ProductPojo();
		p.setBarcode("ndejf");
		p.setBrand_category(1);
		p.setMrp(10.7);
		p.setName("Fanta 200 ml");
		service.add(p);
		double price=11;
		service.checkSellingPrice(price,p.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetIdByBarcodeInvalid() throws ApiException{
		int id=service.getIdByBarcode("gfdg");
	}

//		ProductPojo p=new ProductPojo();
//		p.setProduct("Puma");
//		p.setCategory("Sneakers");
//		service.add(p);
//
//		ProductPojo q=new ProductPojo();
//		q.setProduct("");
//		q.setCategory("Shoes");
//		service.update(p.getId(),q);
//	}
//	@Test(expected = ApiException.class)
//	public void testCategoryEmptyUpdate() throws ApiException{
//		ProductPojo p=new ProductPojo();
//		p.setProduct("Puma");
//		p.setCategory("Sneakers");
//		service.add(p);
//
//		ProductPojo q=new ProductPojo();
//		q.setProduct("Adidas");
//		q.setCategory("");
//		service.update(p.getId(),q);
//	}
//	@Test(expected = ApiException.class)
//	public void testProductCategoryExistUpdate() throws ApiException{
//		ProductPojo p=new ProductPojo();
//		p.setProduct("puma");
//		p.setCategory("sneakers");
//		service.add(p);
//
//		ProductPojo q=new ProductPojo();
//		q.setProduct("puma");
//		q.setCategory("sneakers");
//
//		ProductPojo r=new ProductPojo();
//		r.setProduct("amul");
//		r.setCategory("milk");
//		service.add(r);
//
//		service.update(r.getId(),q);
//	}
//
//	@Test
//	public void testGetAll() throws ApiException{
//		ProductPojo p=new ProductPojo();
//		p.setProduct("puma");
//		p.setCategory("sneakers");
//		service.add(p);
//
//		ProductPojo r=new ProductPojo();
//		r.setProduct("amul");
//		r.setCategory("milk");
//		service.add(r);
//
//		List<ProductPojo> a = service.getAll();
//		assertEquals(2,a.size());
//	}
//
//	@Test(expected = ApiException.class)
//	public void testGetCheck() throws ApiException{
//		service.getValueProductCategory(5);
//		service.getCheck(5);
//	}



}
