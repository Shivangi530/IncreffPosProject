package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {

	private int orderId;
	private int productId;
	private int quantity;
	private double sellingPrice;
	private String barcode;
	private String productName;
	private int inventoryQty;

}
