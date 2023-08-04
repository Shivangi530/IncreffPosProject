package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

	private String barcode;
	private Integer brand_category;
	private String name;
	private double mrp;
	private String brand;
	private String category;

}
