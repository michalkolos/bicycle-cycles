/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Bike {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "number", nullable = false)
	private long number;

	@Column(name = "bike_type", nullable = false)
	private int bikeType;

	@Column(name = "lock_types", nullable = false)
	private String lockTypes;

	@Column(name = "is_electric", nullable = false)
	private boolean isElectric;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "bike",
			cascade={CascadeType.ALL})
	private List<BikeState> states = new ArrayList<>();


	@Override
	public String toString() {
		return "Bike{" +
				"id=" + id +
				", number=" + number +
				", bikeType=" + bikeType +
				", lockTypes='" + lockTypes + '\'' +
				", isElectric=" + isElectric +
				'}';
	}
}
