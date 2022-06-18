/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class BikeState {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@Column(name = "state")
	private String state;

	@Column(name = "electric_lock", nullable = false)
	private boolean isElectricLock;

	@Column(name = "battery_level", nullable = false)
	private int batteryLevel;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "place_id")
	private Place place;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "bike_id", nullable = false)
	private Bike bike;

	@ManyToMany(targetEntity = Sample.class, mappedBy = "bikeStates")
	@ToString.Exclude
	private Set<Sample> samples = new HashSet<>();


	public BikeState() {
	}

	public BikeState(boolean isActive, String state, boolean isElectricLock, int batteryLevel) {
		this.isActive = isActive;
		this.state = state;
		this.isElectricLock = isElectricLock;
		this.batteryLevel = batteryLevel;
	}

	public boolean isSame(BikeState other) {
		return this.getPlace().equals(other.getPlace())
				&& this.getBike().equals(other.getBike())
				&& Math.abs(this.getBatteryLevel() - other.getBatteryLevel()) <= 10;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BikeState bikeState = (BikeState) o;
		return isActive == bikeState.isActive && isElectricLock == bikeState.isElectricLock && place.equals(bikeState.place) && bike.equals(bikeState.bike);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isActive, isElectricLock, place, bike);
	}
}
