/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
	Collection<Weather> findAllByCalculatedTime(Instant calculatedTime);
	Optional<Weather> findByCalculatedTimeAndCity(Instant calculatedTime, City city);
	Optional<Weather> findFirstByCityOrderByCalculatedTimeDesc(City city);
}
