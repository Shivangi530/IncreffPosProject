package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {

	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private double sellingPrice;
	private String barcode;
	private String productName;

}
