/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.OwmWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<OwmWeather, Long> {
	Collection<OwmWeather> findAllByCalculatedTime(Instant calculatedTime);
	Optional<OwmWeather> findByCalculatedTimeAndCity(Instant calculatedTime, City city);
	Optional<OwmWeather> findFirstByCityOrderByCalculatedTimeDesc(City city);
}
