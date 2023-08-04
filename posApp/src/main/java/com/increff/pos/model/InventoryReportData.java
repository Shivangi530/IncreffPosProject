package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReportData extends InventoryForm {
	private Integer id;
	private Integer quantity;
	private String barcode;
	private String brand;
	private String category;
}
