/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.*;
import com.michalkolos.bicyclecycles.persistence.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WeatherDao {

	private final WeatherRepository weatherRepository;
	private final CityDao cityDao;
	private final WeatherConditionDao weatherConditionDao;


	@Autowired
	public WeatherDao(WeatherRepository weatherRepository,
	                  CityDao cityDao,
	                  WeatherConditionDao weatherConditionDao) {

		this.weatherRepository = weatherRepository;
		this.cityDao = cityDao;
		this.weatherConditionDao = weatherConditionDao;
	}

	@Transactional
	public Sample persistSample(Sample sample, Sample previousSample) {
		log.info("Starting persisting new weather data...");
		cityDao.initTransaction();
		weatherConditionDao.initTransaction();

		final Map<City, OwmWeather> previousStates = generateStateMap(previousSample.getOwmWeathers());
		long totalPreviousStatesCount = previousStates.size();
		log.info("Found {} most recent Weather entities in database", totalPreviousStatesCount);

		Set<OwmWeather> currentOwmWeathers = sample.getOwmWeathers().stream()
				.map(unsynced ->
						Optional.ofNullable(previousStates.remove(unsynced.getCity()))
								.map(previous -> choseCurrentWeather(previous, unsynced, previousStates))
								.orElseGet(() -> setWeatherForSyncing(unsynced, previousStates)))
				.collect(Collectors.toSet());

		long unchangedWeathersCount = currentOwmWeathers.stream()
				.filter(weather -> weather.getId() != null)
				.count();
		long totalCurrentWeathersCount = currentOwmWeathers.size();

		sample.setOwmWeathers(currentOwmWeathers);

		log.info("Persisted {} weather data: {} new, {} unchanged from previous. Delta vs previous sample: {}",
				totalCurrentWeathersCount,
				totalCurrentWeathersCount - unchangedWeathersCount,
				unchangedWeathersCount,
				totalCurrentWeathersCount - totalPreviousStatesCount);

		return sample;
	}

	private Map<City, OwmWeather> generateStateMap(Set<OwmWeather> owmWeatherSet) {
		return  owmWeatherSet.stream()
				.collect(Collectors.toMap(OwmWeather::getCity, weather -> weather));
	}


	private OwmWeather choseCurrentWeather(OwmWeather previous,
	                                       OwmWeather notSynced,
	                                       Map<City, OwmWeather> previousStates) {
		return notSynced.equals(previous) ? previous :  setWeatherForSyncing(notSynced, previousStates);
	}

	private OwmWeather setWeatherForSyncing(OwmWeather notSynced,
	                                        Map<City, OwmWeather> previousStates) {
		notSynced.setCity(cityDao.sync(notSynced.getCity()));
		notSynced.setCondition(weatherConditionDao.sync(notSynced.getCondition()));
		previousStates.put(notSynced.getCity(), notSynced);

		return notSynced;
	}
}
