package com.increff.employee.dao;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {

	private static String delete_id = "delete from OrderItemPojo p where id=:id";
	private static String select_id = "select p from OrderItemPojo p where id=:id";
	private static String select_all = "select p from OrderItemPojo p";
	private static String select_relevent = "select p from OrderItemPojo p where orderId=:orderId";

	public List<OrderItemPojo> selectByOrderId(Integer orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_relevent, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderItemPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public OrderItemPojo select(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}
	public List<OrderItemPojo> selectRelevent(List<OrderPojo> allOrderId) {
		List<OrderItemPojo> list=new ArrayList<OrderItemPojo>();
		for (OrderPojo orderId: allOrderId){
			TypedQuery<OrderItemPojo> query = getQuery(select_relevent, OrderItemPojo.class);
			query.setParameter("orderId",orderId.getId());
			list.addAll(query.getResultList());
		}
		return list;
	}
	public List<OrderItemPojo> selectOrderItemByOrderId(int id) {
		List<OrderItemPojo> list=new ArrayList<OrderItemPojo>();
		TypedQuery<OrderItemPojo> query = getQuery(select_relevent, OrderItemPojo.class);
		query.setParameter("orderId",id);
		list.addAll(query.getResultList());
		return list;
	}

	public void update(OrderItemPojo p) {
	}



}
