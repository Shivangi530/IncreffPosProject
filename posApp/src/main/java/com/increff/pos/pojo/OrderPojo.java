package com.increff.pos.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

// todo: read about criteria builder to build native queries

@Entity
public class OrderPojo {

//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
//	@SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1001, allocationSize = 1)

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
	@SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1001, allocationSize = 1)
	private Integer id;

	@Column(nullable = false)
	private LocalDateTime dateTime; //TODO: Use zone date times
//	private boolean status;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	//todo: enums should be present in model
	public static enum Status {
		created,
		invoiced,
		deleted
	}

	public OrderPojo() {
		this.dateTime = LocalDateTime.now();
		this.status = Status.created;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
