package com.increff.employee.dao;

import com.increff.employee.pojo.DayOnDaySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DayOnDaySalesDao extends AbstractDao {

	private static String delete_id = "delete from DayOnDaySalesPojo p where id=:id";
	private static String select_id = "select p from DayOnDaySalesPojo p where id=:id";
	private static String select_all = "select p from DayOnDaySalesPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(DayOnDaySalesPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public DayOnDaySalesPojo select(int id) {
		TypedQuery<DayOnDaySalesPojo> query = getQuery(select_id, DayOnDaySalesPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<DayOnDaySalesPojo> selectAll() {
		TypedQuery<DayOnDaySalesPojo> query = getQuery(select_all, DayOnDaySalesPojo.class);
		return query.getResultList();
	}

	public void update(DayOnDaySalesPojo p) {
	}



}
