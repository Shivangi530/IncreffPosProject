package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrandForm {
	@NotBlank(message = "Brand must not be blank")
	private String brand;
	@NotBlank(message = "Category must not be blank")
	private String category;

}
