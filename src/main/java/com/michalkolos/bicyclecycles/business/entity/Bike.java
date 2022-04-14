/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Bike {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "bike_number", nullable = false, unique = true)
	private long number;

	@Column(name = "bike_type", nullable = false)
	private int bikeType;

	@Column(name = "lock_types")
	private String lockTypes;

	@Column(name = "is_electric", nullable = false)
	private boolean isElectric;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "bike",
			cascade={CascadeType.ALL})
	private List<BikeState> states = new ArrayList<>();


	public Bike() {
	}

	public Bike(long number, int bikeType, String lockTypes, boolean isElectric) {
		this.number = number;
		this.bikeType = bikeType;
		this.lockTypes = lockTypes;
		this.isElectric = isElectric;
	}

	public void addState(BikeState state) {
		state.setBike(this);
		states.add(state);
	}


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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bike bike = (Bike) o;
		return number == bike.number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
}
