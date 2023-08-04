package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.EnumData;
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
	public void delete(Integer id) throws ApiException {
		dao.delete(id);
	}

	@Transactional
	public List<OrderItemPojo> getOrderList(Integer id) throws ApiException {
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
	public void update(Integer id, Integer quantity, double sellingPrice) throws ApiException {
		if(quantity<=0){
			throw new ApiException("Quantity should be positive");
		}if(sellingPrice<=0){
			throw new ApiException("Selling Price should be positive");
		}
		OrderItemPojo existingPojo = getCheck(id);
		existingPojo.setQuantity(quantity);
		existingPojo.setSellingPrice(sellingPrice);
		dao.update(existingPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderItemPojo getCheck(Integer id) throws ApiException {
		OrderItemPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("OrderItem with given ID does not exit, id: " + id);
		}
		return p;
	}
}
