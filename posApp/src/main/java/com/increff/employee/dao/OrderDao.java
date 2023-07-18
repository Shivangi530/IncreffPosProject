package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

	private static String delete_id = "delete from OrderPojo p where id=:id";
	private static String select_id = "select p from OrderPojo p where id=:id";
	private static String select_all = "select p from OrderPojo p";
	private static String select_dates = "SELECT p FROM OrderPojo p WHERE dateTime BETWEEN :startDate AND :endDate AND status = true";
	private static String select_distinct_dates="select p distinct dateTime FROM OrderPojo p";
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

	public List<OrderPojo> selectDates(LocalDateTime startDate, LocalDateTime endDate) {
//		LocalDateTime startDate = startDate1.atStartOfDay();
//		LocalDateTime endDate = endDate1.atTime(LocalTime.MAX);
		TypedQuery<OrderPojo> query = getQuery(select_dates, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
	public List<OrderPojo> selectDistinctDates() {
		TypedQuery<OrderPojo> query = getQuery(select_distinct_dates, OrderPojo.class);
		return query.getResultList();
	}
	public void update(OrderPojo p) {
	}



}
