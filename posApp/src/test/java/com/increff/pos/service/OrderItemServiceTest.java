package com.increff.pos.service;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OutwardOrderPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

		OrderItemPojo q= service.getCheck(p.getId());
		assertEquals(p,q);
		assertEquals(10,(long)q.getOrderId());
		assertEquals(11,(long)q.getProductId());
		assertEquals(10.8,p.getSellingPrice(),0.0001);
		assertEquals(10,(long)p.getQuantity());
	}

	@Test
	public void testInvalidOrderIdAdd() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(0);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);

		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Invalid Order Id", e.getMessage());
		}
	}

	@Test
	public void testInvalidProductIdAdd() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(1);
		p.setProductId(0);
		p.setSellingPrice(10.8);
		p.setQuantity(10);

		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Invalid Product Id", e.getMessage());
		}
	}

	@Test
	public void testInvalidQuantityAdd() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(1);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(-1);

		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Quantity should be positive", e.getMessage());
		}
	}

	@Test
	public void testInvalidSellingPriceAdd() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(1);
		p.setProductId(11);
		p.setSellingPrice(-10.8);
		p.setQuantity(10);

		try {
			service.add(p);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Selling Price should be positive", e.getMessage());
		}
	}

	@Test
	public void testDelete() throws ApiException{
		OutwardOrderPojo outwardOrderPojo = new OutwardOrderPojo();
		orderService.add(outwardOrderPojo);
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(outwardOrderPojo.getId());
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);
		int id=p.getId();
		service.delete(id);
		List<OrderItemPojo>list= service.getAll();
		assertEquals(0,list.size());
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
		OutwardOrderPojo order1= new OutwardOrderPojo();
		orderService.add(order1);

		OutwardOrderPojo order2= new OutwardOrderPojo();
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

		List<OutwardOrderPojo> orderlist= new ArrayList<>();
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

		OrderItemPojo q= service.getCheck(p.getId());
		q.setQuantity(110);
		q.setSellingPrice(90.34);
		service.update(p.getId(),110,90.34);
		assertEquals(90.34,p.getSellingPrice(),0.0001);
		assertEquals(110,(long)p.getQuantity());
	}

	@Test
	public void testInvalidQuantityUpdate() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		try {
			service.update(p.getId(),-110,90.34);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Quantity should be positive", e.getMessage());
		}
	}

	@Test
	public void testInvalidSellingPriceUpdate() throws ApiException{
		OrderItemPojo p=new OrderItemPojo();
		p.setOrderId(10);
		p.setProductId(11);
		p.setSellingPrice(10.8);
		p.setQuantity(10);
		service.add(p);

		try {
			service.update(p.getId(),10,-90.34);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "Selling Price should be positive", e.getMessage());
		}
	}

	@Test
	public void testInvalidGetCheck() throws ApiException{
		try {
			service.getCheck(-1);
			fail("Expected ApiException was not thrown");
		} catch (ApiException e) {
			TestCase.assertEquals( "OrderItem with given ID does not exit, id: -1", e.getMessage());
		}
	}
}
