/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.openweathermaps;

import com.michalkolos.bicyclecycles.business.dao.CityDao;
import com.michalkolos.bicyclecycles.business.dao.WeatherDao;
import com.michalkolos.bicyclecycles.business.entity.City;
import com.michalkolos.bicyclecycles.business.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.entity.Weather;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.dto.OwmCityDto;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class WeatherService {


	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	private final CityDao cityDao;
	private final WeatherDao weatherDao;
	private final OwmDownloadService owmDownloadService;

	ConcurrentHashMap<City, Instant> weatherTimeMap = new ConcurrentHashMap<>();


	@Autowired
	public WeatherService(CityDao cityDao, WeatherDao weatherDao, OwmDownloadService owmDownloadService) {
		this.cityDao = cityDao;
		this.weatherDao = weatherDao;
		this.owmDownloadService = owmDownloadService;

		initWeatherTimeMap();
	}


	private City filOwmApiId(City city) {
		if(city.getOwmId() == null || city.getOwmId() == 0) {
			Point centroid = city.getBounds().getCentroid();
			String cityName = city.getAlias();

			Long apiId = owmDownloadService.getSingleWeather(centroid)
					.map(dto -> {
						logger.info("Downloaded Openweathermaps API ID for city: "
								+ cityName + "  -  " + dto.getId());
						return dto.getId();
					})

					.orElseGet(() -> {
						logger.error("Unable to download Openweathermaps API ID for city: "
								+ cityName);
						return 0L;
					});

			city.setOwmId(apiId);
			city = cityDao.save(city);
		}

		return city;
	}

	@Transactional
	public void initWeatherTimeMap() {
		Instant currentTime = Instant.now();

		cityDao.getAll().values().forEach(city -> {
			Instant calculatedTime = weatherDao.getLastWeatherForCity(city)
							.map(Weather::getCalculatedTime)
							.orElse(Instant.MIN);

			weatherTimeMap.put(city, calculatedTime);
		});
	}

	@Transactional
	public Set<Weather> getAllCityWeather(Snapshot snapshot) {
		Set<City> cities = new HashSet<>(cityDao.getAll().values());

		Set<Long> ids = cities.stream()
				.map(this::filOwmApiId)
				.map(City::getOwmId)
				.filter(id -> id != null && id != 0)
				.collect(Collectors.toSet());

		Set<OwmCityDto> dtos = owmDownloadService.getGroupWeather(ids);

		Set<Weather> weathers = new HashSet<>();

		dtos.forEach( dto -> {
			cities.forEach( city -> {
				if(city.getOwmId().equals(dto.getId())) {
					weathers.add(weatherDao.create(dto, city, snapshot));
				}
			});
		});

		return weathers;
	}

	private boolean isWeatherNew(Weather weather) {
		if(!weatherTimeMap.containsKey(weather.getCity()) ||
		weather.getCalculatedTime().isAfter(weatherTimeMap.get(weather.getCity()))) {

			weatherTimeMap.put(weather.getCity(), weather.getCalculatedTime());
			return true;
		}

		return false;
	}
}
