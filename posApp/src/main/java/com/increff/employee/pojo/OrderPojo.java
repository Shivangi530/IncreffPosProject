package com.increff.employee.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OrderPojo {

//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
//	@SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1001, allocationSize = 1)

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
	@SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1001, allocationSize = 1)
	private Integer id;

	private LocalDateTime dateTime; //TODO: Use zone date times
	private boolean status;
	// TODO: Use enums
	public OrderPojo() {
		this.dateTime = LocalDateTime.now();
		this.status= false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
