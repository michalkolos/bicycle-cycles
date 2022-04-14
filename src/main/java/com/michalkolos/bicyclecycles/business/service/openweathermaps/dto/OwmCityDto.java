/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.openweathermaps.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OwmCityDto {

	private long id;
	private String name;
	private long condition_id;
	private String description;
	private double temp;
	private double human_temp;
	private double rain;
	private double snow;
	private int pressure;
	private int humidity;
	private double wind_speed;
	private int wind_deg;
	private int clouds;
	private int visibility;
	private Instant sunset;
	private Instant sunrise;
	private Instant calculatedTime;
}
