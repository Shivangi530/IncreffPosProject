package com.increff.employee.model;

public class ProductForm {

	private String barcode;
	private int brand_category;
	private String name;
	private double mrp;
	private String brand;
	private String category;
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getBrand_category(){ return brand_category;}
	public void setBrand_category(int brand_category){this.brand_category=brand_category;}

	public double getMrp(){ return mrp;}
	public void setMrp(double mrp){ this.mrp= mrp;}


}
