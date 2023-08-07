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
	private Integer brandCategory;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, scale = 2)
	private double mrp;

}
