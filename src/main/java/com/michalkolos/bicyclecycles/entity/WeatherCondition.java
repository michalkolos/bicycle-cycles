/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class WeatherCondition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "api_id", nullable = false, unique = true)
	private long apiId;

	@Column(name = "description", nullable = false)
	private String description;


	public WeatherCondition() {
	}

	public WeatherCondition(long apiId, String description) {
		this.apiId = apiId;
		this.description = description;
	}
}
