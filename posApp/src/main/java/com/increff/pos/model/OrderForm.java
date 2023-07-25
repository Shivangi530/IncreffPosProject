package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderForm {
	private LocalDateTime dateTime;
	private boolean status;
}