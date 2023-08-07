package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OutwardOrderPojo;
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

	private static String DELETE_ID = "delete from OrderItemPojo p where id=:id";
	private static String SELECT_ID = "select p from OrderItemPojo p where id=:id";
	private static String SELECT_ALL = "select p from OrderItemPojo p";
	private static String SELECT_ITEMS_BY_ORDER = "select p from OrderItemPojo p where orderId=:orderId";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderItemPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public OrderItemPojo select(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectRelevant(List<OutwardOrderPojo> allOrderId) {
		List<OrderItemPojo> list = new ArrayList<>();
		for (OutwardOrderPojo orderId : allOrderId) {
			TypedQuery<OrderItemPojo> query = getQuery(SELECT_ITEMS_BY_ORDER, OrderItemPojo.class);
			query.setParameter("orderId", orderId.getId());
			list.addAll(query.getResultList());
		}
		return list;
	}

	public List<OrderItemPojo> selectOrderItemByOrderId(int id) {
		List<OrderItemPojo> list = new ArrayList<>();
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ITEMS_BY_ORDER, OrderItemPojo.class);
		query.setParameter("orderId", id);
		list.addAll(query.getResultList());
		return list;
	}

	public void update(OrderItemPojo p) {
	}
}
