package com.increff.pos.service;

import com.increff.pos.model.InvoiceForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OutwardOrderPojo;
import com.increff.pos.pojo.ProductPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceTest extends AbstractUnitTest {

//	@Value("${invoice.url}")
//	private String invoiceUrl;

	@Autowired
	private OrderService service;

	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;

	@Test
	public void testAdd() throws ApiException {
		OutwardOrderPojo p = new OutwardOrderPojo();
		service.add(p);

		OutwardOrderPojo q = service.get(p.getId());
		assertEquals(p.getDateTime(), q.getDateTime());
		assertEquals(p.getStatus(), q.getStatus());
	}

	@Test
	public void testGetAll() throws ApiException {
		OutwardOrderPojo p = new OutwardOrderPojo();
		service.add(p);

		OutwardOrderPojo q = new OutwardOrderPojo();
		service.add(q);

		List<OutwardOrderPojo> r = service.getAll();
		assertEquals(2, r.size());
	}

	@Test
	public void testUpdateInvoiced() throws ApiException {
		OutwardOrderPojo p = new OutwardOrderPojo();
		service.add(p);

		service.update(p.getId(),"INVOICED");

		assertEquals("INVOICED",service.get(p.getId()).getStatus().toString());
	}

	@Test
	public void testUpdateCanceled() throws ApiException {
		OutwardOrderPojo p = new OutwardOrderPojo();
		service.add(p);

		service.update(p.getId(),"CANCELED");

		assertEquals("CANCELED",service.get(p.getId()).getStatus().toString());
	}

	@Test
	public void testGetOrderDates() throws ApiException {

		OutwardOrderPojo p = new OutwardOrderPojo();
		service.add(p);

		OutwardOrderPojo q = new OutwardOrderPojo();
		service.add(q);

		service.update(p.getId(),"INVOICED");
		service.update(q.getId(),"INVOICED");
		LocalDateTime startDate = p.getDateTime().minusDays(1);
		LocalDateTime endDate = q.getDateTime().plusDays(1);

		List<OutwardOrderPojo> r = service.getOrderDates(startDate, endDate);
		System.out.println("r.size()=" + r.size());
		assertEquals(2, r.size());
	}

	@Test
	public void testGenerateInvoiceForOrder() throws ApiException {
		OutwardOrderPojo outwardOrderPojo = new OutwardOrderPojo();
		service.add(outwardOrderPojo);

		ProductPojo product1 = new ProductPojo();
		product1.setBarcode("ndejf");
		product1.setBrandCategory(1);
		product1.setMrp(10.7);
		product1.setName("Product 1");
		productService.add(product1);

		ProductPojo product2 = new ProductPojo();
		product2.setBarcode("dasfds");
		product2.setBrandCategory(1);
		product2.setMrp(10.9);
		product2.setName("Product 2");
		productService.add(product2);

		OrderItemPojo p1=new OrderItemPojo();
		p1.setOrderId(outwardOrderPojo.getId());
		p1.setProductId(product1.getId());
		p1.setSellingPrice(100.8);
		p1.setQuantity(210);
		orderItemService.add(p1);

		OrderItemPojo p2=new OrderItemPojo();
		p2.setOrderId(outwardOrderPojo.getId());
		p2.setProductId(product2.getId());
		p2.setSellingPrice(10.8);
		p2.setQuantity(10);
		orderItemService.add(p2);

//		List<OrderItemPojo> orderItemPojoList = service.selectByOrderId(orderPojo.getId());

		InvoiceForm invoiceForm = service.generateInvoiceForOrder(outwardOrderPojo.getId());

		assertNotNull(invoiceForm);
		assertEquals(outwardOrderPojo.getId(), invoiceForm.getOrderId());
		assertNotNull(invoiceForm.getPlacedDate());
		assertEquals(2, invoiceForm.getOrderItemList().size());

	}

	@Test
	public void testGetInvoicePDF() throws ApiException, Exception {
		OutwardOrderPojo outwardOrderPojo = new OutwardOrderPojo();
		service.add(outwardOrderPojo);

		ProductPojo product1 = new ProductPojo();
		product1.setBarcode("ndejf");
		product1.setBrandCategory(1);
		product1.setMrp(10.7);
		product1.setName("Product 1");
		productService.add(product1);

		ProductPojo product2 = new ProductPojo();
		product2.setBarcode("dasfds");
		product2.setBrandCategory(1);
		product2.setMrp(10.9);
		product2.setName("Product 2");
		productService.add(product2);

		OrderItemPojo p1=new OrderItemPojo();
		p1.setOrderId(outwardOrderPojo.getId());
		p1.setProductId(product1.getId());
		p1.setSellingPrice(100.8);
		p1.setQuantity(210);
		orderItemService.add(p1);

		OrderItemPojo p2=new OrderItemPojo();
		p2.setOrderId(outwardOrderPojo.getId());
		p2.setProductId(product2.getId());
		p2.setSellingPrice(10.8);
		p2.setQuantity(10);
		orderItemService.add(p2);

		ResponseEntity<byte[]> response= service.getInvoicePDF(outwardOrderPojo.getId());

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
	}

	@Test
	public void testGetCheckInvalid(){
		try {
			service.getCheck(-1);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Order with given ID does not exit, id: -1", e.getMessage());
		}
	}
}