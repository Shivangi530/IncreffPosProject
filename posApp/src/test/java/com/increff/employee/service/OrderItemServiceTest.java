package com.increff.employee.service;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemServiceTest extends AbstractUnitTest {

	@Autowired
	private OrderItemService service;

	@Autowired
	private OrderService orderService;
	@Test
	public void testAdd() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		OrderItemPojo q= service.get(p.getId());
		assertEquals(p,q);
		assertEquals(10,(long)q.getOrderId());
		assertEquals(11,(long)q.getProductId());
		assertEquals(10.8,p.getSellingPrice(),0.0001);
		assertEquals(10,(long)p.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testDelete() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);
		service.delete(p.getId());
		OrderItemPojo q= service.get(p.getId());
	}

	@Test
	public void testGetOrderList() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		OrderItemPojo p1=new OrderItemPojo();
		p1.setOrderId(10);
		p1.setProductId(12);
		p1.setSellingPrice(100.8);
		p1.setQuantity(210);
		service.add(p1);

		OrderItemPojo p2=new OrderItemPojo();
		p2.setOrderId(14);
		p2.setProductId(11);
		p2.setSellingPrice(10.8);
		p2.setQuantity(10);
		service.add(p2);

		List<OrderItemPojo> q= service.getOrderList(10);
		assertEquals(2,q.size());
	}

	@Test
	public void testGetAll() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		OrderItemPojo p1=new OrderItemPojo();
		p1.setOrderId(10);
		p1.setProductId(12);
		p1.setSellingPrice(100.8);
		p1.setQuantity(210);
		service.add(p1);

		OrderItemPojo p2=new OrderItemPojo();
		p2.setOrderId(14);
		p2.setProductId(11);
		p2.setSellingPrice(10.8);
		p2.setQuantity(10);
		service.add(p2);

		List<OrderItemPojo> q= service.getAll();
		assertEquals(3,q.size());
	}

	@Test
	public void testGetRelevantAll() throws ApiException{
		OrderPojo order1= new OrderPojo();
		orderService.add(order1);

		OrderPojo order2= new OrderPojo();
		orderService.add(order2);

		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(order1.getId());
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		OrderItemPojo p1=new OrderItemPojo();
		p1.setOrderId(order2.getId());
		p1.setProductId(12);
		p1.setSellingPrice(100.8);
		p1.setQuantity(210);
		service.add(p1);

		OrderItemPojo p2=new OrderItemPojo();
		p2.setOrderId(14);
		p2.setProductId(11);
		p2.setSellingPrice(10.8);
		p2.setQuantity(10);
		service.add(p2);

		List<OrderPojo> orderlist= new ArrayList<>();
		orderlist.add(order1);
		orderlist.add(order2);
		List<OrderItemPojo> q= service.getRelevantAll(orderlist);
		assertEquals(2,q.size());

	}

	@Test
	public void testUpdate() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		OrderItemPojo q= service.get(p.getId());
		q.setQuantity(110);
		q.setSellingPrice(90.34);
		service.update(p.getId(),q);
		assertEquals(10,(long)q.getOrderId());
		assertEquals(11,(long)q.getProductId());
		assertEquals(90.34,p.getSellingPrice(),0.0001);
		assertEquals(110,(long)p.getQuantity());
	}

}
