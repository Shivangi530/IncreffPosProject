package com.increff.employee.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="InventoryPojo")
public class InventoryPojo {

	@Id
	@Column(name = "id", columnDefinition = "int")
	private int id;

	@Column(name = "quantity", columnDefinition = "VARCHAR(255)")
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity= quantity;
	}




}
