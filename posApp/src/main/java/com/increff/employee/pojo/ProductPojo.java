package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@Table(name = "ProductPojo")
public class ProductPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@Column(name = "id", columnDefinition = "int")
	private int id;
	//@Column(name = "barcode", columnDefinition = "VARCHAR(255")
	private String barcode;
	//@Column(name = "brand_category", columnDefinition = "int")
	private int brand_category;
	//@Column(name = "name", columnDefinition = "VARCHAR(255")
	private String name;
	//@Column(name = "mrp", columnDefinition = "double")
	private double mrp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getBrand_category() {
		return brand_category;
	}

	public void setBrand_category(int brand_category) {
		this.brand_category = brand_category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}



}
