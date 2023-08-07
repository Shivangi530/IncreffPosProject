package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
	private static String SELECT_ID = "select p from BrandPojo p where id=:id";
	private static String SELECT_ALL = "select p from BrandPojo p order by id";
	private static String CHECK_COMBINATION = "select p from BrandPojo p where brand=:brand and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}

	public BrandPojo select(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public BrandPojo checkCombination(String brand,String category) {
		TypedQuery<BrandPojo> query = getQuery(CHECK_COMBINATION, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}

}
