package com.increff.pos.pojo;

import com.increff.pos.model.EnumData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

// todo: read about criteria builder to build native queries

@Entity
@Getter
@Setter
public class OrderPojo extends AbstractPojo{

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeqGen")
	@SequenceGenerator(name = "orderSeqGen", sequenceName = "orderSeqGen", initialValue = 1001, allocationSize = 1)
	private Integer id;

	@Column(nullable = false)
	private LocalDateTime dateTime;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EnumData.Status status;


	public OrderPojo() {
		this.dateTime = LocalDateTime.now();
		this.status = EnumData.Status.CREATED;
	}
}
