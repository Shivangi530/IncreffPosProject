package com.increff.employee.service;

import com.increff.employee.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderServiceTest extends AbstractUnitTest {

	@Autowired
	private OrderService service;

	@Test
	public void testAdd() throws ApiException {
		OrderPojo p = new OrderPojo();
		service.add(p);

		OrderPojo q = service.get(p.getId());
		assertEquals(p.getDateTime(), q.getDateTime());
		assertEquals(p.getStatus(), q.getStatus());
	}

	@Test
	public void testGetAll() throws ApiException {
		OrderPojo p = new OrderPojo();
		service.add(p);

		OrderPojo q = new OrderPojo();
		service.add(q);

		List<OrderPojo> r = service.getAll();
		System.out.println("r.size()=" + r.size());
		assertEquals(2, r.size());
	}

	@Test(expected = ApiException.class)
	public void testDelete() throws ApiException {
		OrderPojo p = new OrderPojo();
		service.add(p);
		service.delete(p.getId());
		service.get(p.getId());
	}

	@Test
	public void testUpdate() throws ApiException {
		OrderPojo p = new OrderPojo();
		service.add(p);

		service.update(p.getId());
		service.get(p.getId());
		assertTrue(p.getStatus());
	}

	@Test
	public void testGetOrderDates() throws ApiException {
		LocalDateTime startDate = LocalDateTime.now();
		OrderPojo p = new OrderPojo();
		service.add(p);

		OrderPojo q = new OrderPojo();
		service.add(q);
		LocalDateTime endDate = LocalDateTime.now();

		List<OrderPojo> r = service.getOrderDates(startDate, endDate);
		System.out.println("r.size()=" + r.size());
		assertEquals(2, r.size());
	}

	@Test
	public void testGenerateInvoiceForOrder() throws ApiException {
		OrderPojo p = new OrderPojo();
		service.add(p);
		service.generateInvoiceForOrder(p.getId());
	}

	@Test
	public void testGetInvoicePDF() throws ApiException, Exception {
		OrderPojo p = new OrderPojo();
		service.add(p);
		service.getInvoicePDF(p.getId());
	}
}