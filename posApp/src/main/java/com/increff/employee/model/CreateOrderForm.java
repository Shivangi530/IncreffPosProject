package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderForm {
	private String barcode;
	private int quantity;
	private double sellingPrice;
}
