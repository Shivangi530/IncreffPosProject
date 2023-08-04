package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ProductPojo extends AbstractPojo{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String barcode;

	@Column(nullable = false)
	private Integer brand_category;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private double mrp;

}
