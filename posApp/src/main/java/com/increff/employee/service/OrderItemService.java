package com.increff.employee.service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	@Autowired
	private InventoryService inventoryService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderItemPojo p) throws ApiException {
//		normalize(p);
//		if(StringUtil.isEmpty(p.getName())) {
//			throw new ApiException("name cannot be empty");
//		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
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
		return dao.selectRelevent(p);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {
		OrderItemPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		ex.setSellingPrice(p.getSellingPrice());
		dao.update(ex);
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
