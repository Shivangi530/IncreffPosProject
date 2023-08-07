package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(	uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})})
@Getter
@Setter
public class BrandPojo extends AbstractPojo{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String brand;

	@Column(nullable = false)
	private String category;
}
