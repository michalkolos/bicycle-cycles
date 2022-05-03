/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Log4j2
public class Sample implements Comparable<Sample>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;



	@ManyToMany(cascade = {CascadeType.ALL, CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(name = "sample_bike_states",
			joinColumns = {@JoinColumn(name = "sample_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "bike_state_id", referencedColumnName = "id")})
	@ToString.Exclude
	private Set<BikeState> bikeStates = new HashSet<>();


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "sample_weather",
			joinColumns = {@JoinColumn(name = "sample_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "weather_id", referencedColumnName = "id")})
	@ToString.Exclude
	private Set<Weather> weathers = new HashSet<>();


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
//		state.getSamples().add(this);
	}

	public void removeBikeState(BikeState state) {
		if(!bikeStates.remove(state)) {
			log.error("While removing BikeState from Sample, did not exist in Sample's set");
		}
//		if(!state.getSamples().remove(this)) {
//			log.error("While removing BikeState from Sample, Sample did not exist in BikeState's set");
//		}
	}

	public void addWeather(Weather weather) {
		if(weather == null) {
			log.error("Unable to add Weather to snapshot: NPE!");
			return;
		}
		if(weather.getCity() == null) {
			log.error("Unable to add Weather to snapshot: no city assigned to weather.");
			return;
		}
		weathers.add(weather);
		weather.getSamples().add(this);
	}

	public void addWeather(Collection<Weather> incomingWeathers) {
		if(weathers == null) {
			weathers = new HashSet<>(incomingWeathers);

		} else {
			weathers.addAll(incomingWeathers);
		}

	}

	@Override
	public int compareTo(Sample sample) {
		return this.getTimestamp().compareTo(sample.getTimestamp());
	}
}
