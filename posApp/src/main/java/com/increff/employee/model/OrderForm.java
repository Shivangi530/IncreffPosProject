package com.increff.employee.model;

import java.time.LocalDateTime;

public class OrderForm {

	private LocalDateTime dateTime;
	private boolean status;
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
