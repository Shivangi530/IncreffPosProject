package com.increff.pos.dao;

import com.increff.pos.pojo.DayOnDaySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DayOnDaySalesDao extends AbstractDao {

	private static String DELETE_ID = "delete from DayOnDaySalesPojo p where id=:id";
	private static String SELECT_ID = "select p from DayOnDaySalesPojo p where id=:id";
	private static String SELECT_ALL = "select p from DayOnDaySalesPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(DayOnDaySalesPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public DayOnDaySalesPojo select(int id) {
		TypedQuery<DayOnDaySalesPojo> query = getQuery(SELECT_ID, DayOnDaySalesPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<DayOnDaySalesPojo> selectAll() {
		TypedQuery<DayOnDaySalesPojo> query = getQuery(SELECT_ALL, DayOnDaySalesPojo.class);
		return query.getResultList();
	}

	public void update(DayOnDaySalesPojo p) {
	}
}
