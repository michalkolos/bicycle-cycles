/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class CityStats {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "snapshot_id")
	private Snapshot snapshot;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "city_id")
	private City city;

	@Column(name = "booked_bikes")
	private int bookedBikes;

	@Column(name = "set_point_bikes")
	private int setPointBikes;

	@Column(name = "available_bikes")
	private int availableBikes;


	public CityStats() {
	}

	public CityStats(int bookedBikes, int setPointBikes, int availableBikes) {
		this.bookedBikes = bookedBikes;
		this.setPointBikes = setPointBikes;
		this.availableBikes = availableBikes;
	}
}
