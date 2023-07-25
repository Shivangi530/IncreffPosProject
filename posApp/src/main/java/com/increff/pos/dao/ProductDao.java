package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

	private static String SELECT_ID = "select p from ProductPojo p where id=:id";
	private static String SELECT_ALL = "select p from ProductPojo p";
	private static String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
	}

	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public ProductPojo checkId(int id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public ProductPojo checkBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public String selectBarcode(int id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query).getBarcode();
	}

	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		return query.getResultList();
	}

	public void update(ProductPojo p) {
	}
}
