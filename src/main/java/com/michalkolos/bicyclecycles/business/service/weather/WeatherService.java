/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.persistence.dao.CityDao;
import com.michalkolos.bicyclecycles.persistence.dao.SnapshotDao;
import com.michalkolos.bicyclecycles.persistence.dao.WeatherDao;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.OwmWeatherSource;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	private final CityDao cityDao;
	private final WeatherDao weatherDao;
	private final SnapshotDao snapshotDao;
	private final WeatherSource weatherSource;

	ConcurrentHashMap<City, Instant> weatherTimeMap = new ConcurrentHashMap<>();

	@Autowired
	public WeatherService(CityDao cityDao, WeatherDao weatherDao, SnapshotDao snapshotDao, WeatherSource weatherSource) {
		this.cityDao = cityDao;
		this.weatherDao = weatherDao;
		this.snapshotDao = snapshotDao;
		this.weatherSource = weatherSource;
	}

	public void downloadCurrent(Snapshot snapshot) {
		List<City> allCities = cityDao.getAll();
		Set<Weather> currentWeathers = weatherSource.getGroupWeather(allCities);
		snapshot.addWeather(currentWeathers);
		snapshotDao.save(snapshot);
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
