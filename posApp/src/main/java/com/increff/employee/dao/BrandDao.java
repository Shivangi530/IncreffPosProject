package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

	private static String DELETE_ID = "delete from BrandPojo p where id=:id";
	private static String SELECT_ID = "select p from BrandPojo p where id=:id";
	private static String SELECT_ALL = "select p from BrandPojo p order by id";
	private static String checkCombination = "select p from BrandPojo p where brand=:brand and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public BrandPojo checkCombination(String brand,String category) {
		TypedQuery<BrandPojo> query = getQuery(checkCombination, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}
	public BrandPojo checkCategory(int id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id",id);
		return getSingle(query);
	}


	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}

	public void update(BrandPojo p) {
	}



}
