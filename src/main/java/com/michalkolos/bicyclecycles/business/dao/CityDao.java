/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.dao;

import com.michalkolos.bicyclecycles.business.entity.City;
import com.michalkolos.bicyclecycles.business.entity.CityStats;
import com.michalkolos.bicyclecycles.business.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CityDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CountryDto;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.OwmDownloadService;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.WeatherService;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import com.michalkolos.bicyclecycles.persistence.repository.CityStatsRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CityDao {

	private static final Logger logger = LoggerFactory.getLogger(CityDao.class);

	private final CityRepository cityRepository;
	private final OwmDownloadService owmDownloadService;

	@Autowired
	public CityDao(CityRepository cityRepository, OwmDownloadService owmDownloadService) {
		this.cityRepository = cityRepository;
		this.owmDownloadService = owmDownloadService;
	}


	@Transactional
	public City update(Snapshot snapshot, CountryDto countryDto, CityDto cityDto) {

		City city = find(cityDto.getUid()).orElse(create(cityDto, countryDto));

		CityStats stats = new CityStats(
				cityDto.getBooked_bikes(),
				cityDto.getSet_point_bikes(),
				cityDto.getAvailable_bikes());

		snapshot.addCityStats(stats);
		city.addStats(stats);

		return city;
	}

	@Transactional
	public Optional<City> find(long uid) {
		return cityRepository.findByUid(uid);
	}

	@Transactional
	public City create(CityDto cityDto, CountryDto countryDto){
		return cityRepository.save(new City(cityDto, countryDto));
	}


	@Transactional
	public Map<Long, City> getAll() {
		Map<Long, City> cityMap = new HashMap<>();
		cityRepository.findAll()
				.forEach((City city) -> cityMap.put(city.getUid(), city));

		return cityMap;
	}





	public void save(Iterable<City> cities) {
		cityRepository.saveAll(cities);
	}

	public City save(City city) {
		return cityRepository.save(city);
	}
}
