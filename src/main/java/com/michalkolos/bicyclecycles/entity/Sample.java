/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import com.michalkolos.bicyclecycles.persistence.dao.BikeStateDao;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Log4j2
public class Sample implements Comparable<Sample> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;

	@Column(name = "creation_duration")
	private Duration creationDuration;

	@ManyToMany(targetEntity = BikeState.class,
			cascade = {CascadeType.ALL, CascadeType.ALL},
			fetch = FetchType.LAZY)
	@JoinTable(name = "sample_bike_states",
			joinColumns = {@JoinColumn(name = "sample_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "bike_state_id", referencedColumnName = "id")})
	@ToString.Exclude
	private Set<BikeState> bikeStates = new HashSet<>();


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "sample_owm_weather",
			joinColumns = {@JoinColumn(name = "sample_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "weather_id", referencedColumnName = "id")})
	@ToString.Exclude
	private Set<OwmWeather> owmWeathers = new HashSet<>();


	public Sample() {
		this.timestamp = Instant.now();
	}

	public Sample(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public void addBikeState(BikeState state) {
		if(state == null) {
			log.error("Unable to add BikeState to snapshot: NPE!");
			return;
		}
		if(state.getPlace() == null) {
			log.error("Unable to add BikeState to snapshot: no place assigned to state.");
			return;
		}

		bikeStates.add(state);
		state.getSamples().add(this);
	}

	public void setBikeStates(Set<BikeState> bikeStates) {
		bikeStates.forEach(bikeState -> bikeState.getSamples().add(this));
		this.bikeStates = bikeStates;
	}

	public void addWeather(OwmWeather owmWeather) {
		if(owmWeather == null) {
			log.error("Unable to add Weather to snapshot: NPE!");
			return;
		}
		if(owmWeather.getCity() == null) {
			log.error("Unable to add Weather to snapshot: no city assigned to weather.");
			return;
		}
		owmWeathers.add(owmWeather);
	}

	public void addWeather(Collection<OwmWeather> incomingOwmWeathers) {
			owmWeathers.addAll(incomingOwmWeathers);
	}

	@Override
	public int compareTo(Sample sample) {
		return this.getTimestamp().compareTo(sample.getTimestamp());
	}
}
