/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.entity.OwmWeatherCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherConditionRepository extends JpaRepository<OwmWeatherCondition, Long> {
	Optional<OwmWeatherCondition> findByApiId(Long apiId);
}
