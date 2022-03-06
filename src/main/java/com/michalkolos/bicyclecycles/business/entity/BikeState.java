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

	@Column(name = "state", nullable = false)
	private String state;

	@Column(name = "electric_lock", nullable = false)
	private boolean isElectricLock;

	@Column(name = "battery_level", nullable = false)
	private int batteryLevel;

	@ManyToOne
	@JoinColumn(name = "bike_id")
	private Bike bike;

	@ManyToOne
	@JoinColumn(name = "snapshot_id")
	private Snapshot snapshot;
}
