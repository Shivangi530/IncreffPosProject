package com.increff.pos.service;

import com.increff.pos.pojo.OutwardOrderPojo;
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
		OutwardOrderPojo p = new OutwardOrderPojo();
		orderService.add(p);

		service.add();

	}
}
