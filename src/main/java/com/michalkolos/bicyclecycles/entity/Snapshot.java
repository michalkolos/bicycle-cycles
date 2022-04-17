/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

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

	@OneToMany(fetch=FetchType.EAGER, mappedBy = "snapshot",
			cascade={CascadeType.ALL})
	@Column(name="bike_states")
	private Set<BikeState> bikeStates = new HashSet<>();

//	@OneToMany(fetch=FetchType.EAGER, mappedBy = "snapshot",
//			cascade={CascadeType.ALL})
//	@Column(name="weather")

//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinTable(name="author_book", joinColumns=@JoinColumn(name="book_id"), inverseJoinColumns=@JoinColumn(name="author_id"))

//	@ManyToMany(
//			fetch=FetchType.EAGER,
//			cascade=CascadeType.ALL)
//	@JoinTable(
//			name = "weather",
//			joinColumns = @JoinColumn(name = "snapshot_id"),
//			inverseJoinColumns = @JoinColumn(name = "weather_id"))

	@ManyToMany
	private Set<Weather> weathers = new HashSet<>();


	public Snapshot() {
		this.timestamp = Instant.now();
	}

	public Snapshot(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public void addBikeState(BikeState state) {
		bikeStates.add(state);
		state.setSnapshot(this);
	}

	public void addWeather(Weather weather) {
		weathers.add(weather);
	}

	public void addWeather(Collection<Weather> incomingWeathers) {
		if(weathers == null) {
			weathers = new HashSet<>(incomingWeathers);

		} else {
			weathers.addAll(incomingWeathers);
		}

	}

	@Override
	public String toString() {
		return "Snapshot{" +
				"id=" + id +
				", timestamp=" + timestamp +
				'}';
	}
}
