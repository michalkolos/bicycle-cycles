/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class OwmWeatherCondition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "api_id", nullable = false)
	private long apiId;

	@Column(name = "main_name", nullable = false)
	private String mainName;

	@Column(name = "description", nullable = false)
	private String description;


	public OwmWeatherCondition() {
	}

	public OwmWeatherCondition(long apiId, String mainName, String description) {
		this.apiId = apiId;
		this.mainName = mainName;
		this.description = description;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OwmWeatherCondition that = (OwmWeatherCondition) o;
		return apiId == that.apiId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apiId);
	}
}
