package com.increff.pos.pojo;

import javax.persistence.*;

@Entity
public class BrandPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	// TODO "optimistic locking" create an abstract class for createAt and updateAt and version

	@Column(nullable = false, length = 255)
	private String brand;
	@Column(nullable = false, length = 255)
	private String category;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
