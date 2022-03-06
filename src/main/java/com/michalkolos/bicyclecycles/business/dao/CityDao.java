/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.dao;

import com.michalkolos.bicyclecycles.business.entity.City;
import com.michalkolos.bicyclecycles.business.entity.CityStats;
import com.michalkolos.bicyclecycles.business.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CityDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CountryDto;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import com.michalkolos.bicyclecycles.persistence.repository.CityStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CityDao {
	private final CityRepository cityRepository;
	private final CityStatsRepository cityStatsRepository;

	@Autowired
	public CityDao(CityRepository cityRepository, CityStatsRepository cityStatsRepository) {
		this.cityRepository = cityRepository;
		this.cityStatsRepository = cityStatsRepository;
	}

	public City updateCityStats(Snapshot snapshot, CountryDto countryDto, CityDto cityDto) {

		City city = cityRepository
				.findByUid(cityDto.getUid())
				.orElse(cityRepository.save(new City(cityDto, countryDto)));

		CityStats stats = new CityStats(
				cityDto.getBooked_bikes(),
				cityDto.getSet_point_bikes(),
				cityDto.getAvailable_bikes());

		snapshot.addCityStats(stats);
		city.addStats(stats);

		return city;
	}

	public void saveChanges(Iterable<City> cities) {
		cityRepository.saveAll(cities);
	}

}
