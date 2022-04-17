/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
public class Weather {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "temp", nullable = false)
	private double temp;

	@Column(name = "human_temp", nullable = false)
	private double humanTemp;

	@Column(name = "pressure", nullable = false)
	private int pressure;

	@Column(name = "rain", nullable = false)
	private double rain;

	@Column(name = "snow", nullable = false)
	private double snow;

	@Column(name = "humidity", nullable = false)
	private int humidity;

	@Column(name = "wind_speed", nullable = false)
	private double windSpeed;

	@Column(name = "wind_deg", nullable = false)
	private int windDeg;

	@Column(name = "clouds", nullable = false)
	private int clouds;

	@Column(name = "visibility", nullable = false)
	private int visibility;

	@Column(name = "sunset", nullable = false)
	private Instant sunset;

	@Column(name = "sunrise", nullable = false)
	private Instant sunrise;

	@Column(name = "calculated_time", nullable = false)
	private Instant calculatedTime;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "condition_id", nullable = false)
	private WeatherCondition condition;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "city_id", nullable = false)
	private City city;

//	TODO: Remove legacy association (commented out)
//	@ManyToOne(cascade=CascadeType.ALL)
//	@JoinColumn(name = "snapshot_id", nullable = false)
//	private Set<Snapshot> snapshots = new HashSet<>();


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Weather weather = (Weather) o;
		return calculatedTime.equals(weather.calculatedTime) && Objects.equals(city, weather.city);
	}

	@Override
	public int hashCode() {
		return Objects.hash(calculatedTime, city);
	}
}
