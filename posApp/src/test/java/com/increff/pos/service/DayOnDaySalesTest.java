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

//		DayOnDaySalesPojo p=new DayOnDaySalesPojo();
//		p.setDate();
//		p.setOrderCount(1);
//		p.setItemCount(1);
//		p.setRevenue(1.1);
		service.add();

		Integer expectedOrderCount;
		Integer expectedItemCount;
		double expectedRevenue;

//		DayOnDaySalesPojo q= service.checkCombination(expectedDayOnDaySales,expectedCategory);
//		assertEquals(expectedDayOnDaySales,q.getDayOnDaySales());
//		assertEquals(expectedCategory,q.getCategory());
	}
}
