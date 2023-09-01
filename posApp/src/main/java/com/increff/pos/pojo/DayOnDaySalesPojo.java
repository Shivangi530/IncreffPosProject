package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DayOnDaySalesPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(nullable = false, unique = true)
    private LocalDate date;

	@Column(nullable = false)
    private Integer orderCount;

	@Column(nullable = false)
    private Integer itemCount;

	@Column(nullable = false, scale = 2)
    private Double revenue;
}
