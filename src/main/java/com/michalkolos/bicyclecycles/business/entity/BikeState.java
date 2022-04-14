/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "place", nullable = false)
	private Place place;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "bike_id", nullable = false)
	private Bike bike;

	@ManyToOne
	@JoinColumn(name = "snapshot_id", nullable = false)
	private Snapshot snapshot;


	public BikeState() {
	}

	public BikeState(boolean isActive, String state, boolean isElectricLock, int batteryLevel, Place place) {
		this.isActive = isActive;
		this.state = state;
		this.isElectricLock = isElectricLock;
		this.batteryLevel = batteryLevel;
		this.place = place;
	}
}
