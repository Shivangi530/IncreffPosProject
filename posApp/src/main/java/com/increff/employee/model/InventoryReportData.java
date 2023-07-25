package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReportData extends InventoryForm {
	private int id;
	private int quantity;
	private String barcode;
	private String brand;
	private String category;
}
