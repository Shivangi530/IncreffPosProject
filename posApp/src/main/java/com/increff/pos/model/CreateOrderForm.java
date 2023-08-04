package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderForm {
	private String barcode;
	private Integer quantity;
	private double sellingPrice;
}
