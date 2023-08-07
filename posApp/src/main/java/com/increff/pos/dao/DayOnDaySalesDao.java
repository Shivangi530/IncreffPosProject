package com.increff.pos.dao;

import com.increff.pos.pojo.DayOnDaySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DayOnDaySalesDao extends AbstractDao {

	private static String SELECT_ALL = "select p from DayOnDaySalesPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(DayOnDaySalesPojo p) {
		em.persist(p);
	}

	public List<DayOnDaySalesPojo> selectAll() {
		TypedQuery<DayOnDaySalesPojo> query = getQuery(SELECT_ALL, DayOnDaySalesPojo.class);
		return query.getResultList();
	}
}
