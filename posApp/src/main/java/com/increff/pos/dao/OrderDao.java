package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

//	private static String DELETE_ID = "delete from OrderPojo p where id=:id";
	private static String SELECT_ID = "select p from OrderPojo p where id=:id";
	private static String SELECT_ALL = "select p from OrderPojo p";
	private static String SELECT_DATES = "SELECT p FROM OrderPojo p WHERE dateTime BETWEEN :startDate AND :endDate AND status = 'invoiced'";
//	private static String SELECT_DISTINCT_DATES = "select distinct p.dateTime FROM OrderPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderPojo p) {
		em.persist(p);
	}

//	public int delete(int id) {
//		Query query = em.createQuery(DELETE_ID);
//		query.setParameter("id", id);
//		return query.executeUpdate();
//	}

	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ID, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
		return query.getResultList();
	}

	public List<OrderPojo> selectDates(LocalDateTime startDate, LocalDateTime endDate) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_DATES, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

//	public List<Date> selectDistinctDates() {
//		TypedQuery<Date> query = getQuery(SELECT_DISTINCT_DATES, Date.class);
//		return query.getResultList();
//	}

	public void update(OrderPojo p) {
	}
}
