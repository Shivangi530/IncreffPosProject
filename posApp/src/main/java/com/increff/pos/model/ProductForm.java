package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

	private String barcode;
	private int brand_category;
	private String name;
	private double mrp;
	private String brand;
	private String category;

}
