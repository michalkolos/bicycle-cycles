/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CityDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CountryDto;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.OwmWeatherSource;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class CityDao {

	private static final Logger logger = LoggerFactory.getLogger(CityDao.class);

	private final CityRepository cityRepository;
	private final OwmWeatherSource weatherSource;

	@Autowired
	public CityDao(CityRepository cityRepository, OwmWeatherSource weatherSource) {
		this.cityRepository = cityRepository;
		this.weatherSource = weatherSource;
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
	public List<City> getAll() {
		return cityRepository.findAll();
	}

	@Transactional
	public Map<Long, City> getAllMap() {
		Map<Long, City> cityMap = new HashMap<>();
		cityRepository.findAll().forEach(city -> cityMap.put(city.getUid(), city));

		return cityMap;
	}




	public void save(Iterable<City> cities) {
		cityRepository.saveAll(cities);
	}

	public City save(City city) {
		return cityRepository.save(city);
	}
}
