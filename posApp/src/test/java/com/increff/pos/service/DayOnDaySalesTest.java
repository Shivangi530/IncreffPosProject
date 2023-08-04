package com.increff.pos.service;

import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class DayOnDaySalesTest extends AbstractUnitTest {

	@Autowired
	private DayOnDaySalesService service;
	@Autowired
	private OrderService orderService;

	@Test
	public void testAdd() throws ApiException{
		OrderPojo p = new OrderPojo();
		orderService.add(p);

		service.add();

	}
}
