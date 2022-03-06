/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Snapshot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "snapshot",
			cascade={CascadeType.ALL})
	private List<CityStats> cityStatsList = new ArrayList<>();

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "snapshot",
			cascade={CascadeType.ALL})
	private List<BikeState> bikeStatesList = new ArrayList<>();



	public Snapshot() {
		this.timestamp = Instant.now();
	}

	public Snapshot(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public void addCityStats(CityStats stats) {
		cityStatsList.add(stats);
		stats.setSnapshot(this);
	}

	public void addBikeState(BikeState state) {
		bikeStatesList.add(state);
		state.setSnapshot(this);
	}

	@Override
	public String toString() {
		return "Snapshot{" +
				"id=" + id +
				", timestamp=" + timestamp +
				'}';
	}
}
