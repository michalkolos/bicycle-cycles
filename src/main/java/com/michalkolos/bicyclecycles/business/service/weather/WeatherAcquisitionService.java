/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.dao.CityDao;
import com.michalkolos.bicyclecycles.persistence.dao.SampleDao;
import com.michalkolos.bicyclecycles.persistence.dao.WeatherDao;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class WeatherAcquisitionService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherAcquisitionService.class);

	private final CityDao cityDao;
	private final WeatherDao weatherDao;
	private final SampleDao sampleDao;
	private final WeatherSource weatherSource;

	ConcurrentHashMap<City, Instant> weatherTimeMap = new ConcurrentHashMap<>();

	@Autowired
	public WeatherAcquisitionService(CityDao cityDao, WeatherDao weatherDao, SampleDao sampleDao, WeatherSource weatherSource) {
		this.cityDao = cityDao;
		this.weatherDao = weatherDao;
		this.sampleDao = sampleDao;
		this.weatherSource = weatherSource;
	}

	public void downloadCurrent(Sample sample) {
		List<City> allCities = cityDao.getAll();
		Set<Weather> currentWeathers = weatherSource.get(allCities);
		sample.addWeather(currentWeathers);
		sampleDao.persistWeather(sample);
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
