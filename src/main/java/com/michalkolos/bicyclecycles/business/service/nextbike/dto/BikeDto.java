/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike.dto;


import lombok.Data;

import java.util.Objects;


@Data
public class BikeDto {

	private long number;
	private int bike_type;
	private String lock_types;
	private int active;
	private String state;
	private int electric_lock;
	private long boardcomputer;
	private boolean isElectric = false;
	private int pedelec_battery;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BikeDto bikeDto = (BikeDto) o;
		return number == bikeDto.number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
}

