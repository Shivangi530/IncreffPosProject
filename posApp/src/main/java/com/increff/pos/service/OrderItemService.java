package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private OrderService orderService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderItemPojo p) throws ApiException {
		if(p.getOrderId()==0){
			throw new ApiException("Invalid Order Id");
		}if(p.getProductId()<=0){
			throw new ApiException("Invalid Product Id");
		}if(p.getQuantity()<=0){
			throw new ApiException("Quantity should be positive");
		}if(p.getSellingPrice()<=0){
			throw new ApiException("Selling Price should be positive");
		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) throws ApiException {
		OrderPojo.Status status=orderService.get(get(id).getOrderId()).getStatus();
		if(status== OrderPojo.Status.invoiced){
			throw new ApiException("Cannot delete OrderItem, Invoice already generated");
		}
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderItemPojo get(int id) throws ApiException {
		return getCheck(id);
	}
	@Transactional
	public List<OrderItemPojo> getOrderList(int id) throws ApiException {
		return dao.selectOrderItemByOrderId(id);
	}

	@Transactional
	public List<OrderItemPojo> getAll() {
		return dao.selectAll();
	}
	@Transactional
	public List<OrderItemPojo> getRelevantAll(List<OrderPojo> p) {
		return dao.selectRelevant(p);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {
		if(p.getQuantity()<=0){
			throw new ApiException("Quantity should be positive");
		}if(p.getSellingPrice()<=0){
			throw new ApiException("Selling Price should be positive");
		}
		OrderItemPojo existingPojo = getCheck(id);
		existingPojo.setQuantity(p.getQuantity());
		existingPojo.setSellingPrice(p.getSellingPrice());
		dao.update(existingPojo);
	}

	@Transactional
	public OrderItemPojo getCheck(int id) throws ApiException {
		OrderItemPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("OrderItem with given ID does not exit, id: " + id);
		}
		return p;
	}
	public List<OrderItemPojo> selectByOrderId(Integer orderId) {
		return dao.selectByOrderId(orderId);
	}
}
