package com.increff.employee.pojo;

import javax.persistence.*;

@Entity
// TODO: Add index for the column
public class ProductPojo {

//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, unique = true)
	private String barcode;
	private Integer brand_category;
	@Column(nullable = false)
	private String name;
	private double mrp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getBrand_category() {
		return brand_category;
	}

	public void setBrand_category(Integer brand_category) {
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
