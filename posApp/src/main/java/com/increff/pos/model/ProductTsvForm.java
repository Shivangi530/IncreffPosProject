package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTsvForm {
	private String barcode;
	private String name;
	private String mrp;
	private String brand;
	private String category;
}
