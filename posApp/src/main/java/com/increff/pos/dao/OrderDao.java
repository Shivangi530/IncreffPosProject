package com.increff.pos.dao;

import com.increff.pos.pojo.OutwardOrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

	private static String SELECT_ID = "select p from OutwardOrderPojo p where id=:id";
	private static String SELECT_ALL = "select p from OutwardOrderPojo p";
	private static String SELECT_DATES = "SELECT p FROM OutwardOrderPojo p WHERE dateTime BETWEEN :startDate AND :endDate AND status = 'INVOICED'";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OutwardOrderPojo p) {
		em.persist(p);
	}

	public OutwardOrderPojo select(int id) {
		TypedQuery<OutwardOrderPojo> query = getQuery(SELECT_ID, OutwardOrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OutwardOrderPojo> selectAll() {
		TypedQuery<OutwardOrderPojo> query = getQuery(SELECT_ALL, OutwardOrderPojo.class);
		return query.getResultList();
	}

	public List<OutwardOrderPojo> selectDates(LocalDateTime startDate, LocalDateTime endDate) {
		TypedQuery<OutwardOrderPojo> query = getQuery(SELECT_DATES, OutwardOrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
}

