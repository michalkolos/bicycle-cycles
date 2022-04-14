/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.business.entity.Weather;
import com.michalkolos.bicyclecycles.business.entity.WeatherCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherConditionRepository extends JpaRepository<WeatherCondition, Long> {
	Optional<WeatherCondition> findByApiId(Long apiId);
}
