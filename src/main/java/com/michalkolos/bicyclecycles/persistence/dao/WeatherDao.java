/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.*;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.persistence.repository.WeatherConditionRepository;
import com.michalkolos.bicyclecycles.persistence.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WeatherDao {

	private final WeatherRepository weatherRepository;
	private final WeatherConditionRepository weatherConditionRepository;


	private final CityDao cityDao;
	private final SampleDao sampleDao;
	private final WeatherConditionDao weatherConditionDao;


	@Autowired
	public WeatherDao(WeatherRepository weatherRepository,
	                  WeatherConditionRepository weatherConditionRepository,
	                  CityDao cityDao,
	                  SampleDao sampleDao,
	                  WeatherConditionDao weatherConditionDao) {

		this.weatherRepository = weatherRepository;
		this.weatherConditionRepository = weatherConditionRepository;
		this.cityDao = cityDao;
		this.sampleDao = sampleDao;
		this.weatherConditionDao = weatherConditionDao;
	}

	private Map<City, Weather> previousStates = new ConcurrentHashMap<>();

	@Transactional
	public Sample persistSample(Sample sample) {
		log.info("Starting persisting new weather data...");

		buildLocalEntityContext();

		long totalPreviousStatesCount = previousStates.size();

		Set<Weather> currentWeathers = sample.getWeathers().stream()
				.map(unsynced ->
						Optional.ofNullable(previousStates.remove(unsynced.getCity()))
								.map(previous -> choseCurrentWeather(previous, unsynced))
								.orElseGet(() -> setWeatherForSyncing(unsynced)))
				.collect(Collectors.toSet());

		long unchangedWeathersCount = currentWeathers.stream()
				.filter(weather -> weather.getId() != null)
				.count();
		long totalCurrentWeathersCount = currentWeathers.size();

		sample.setWeathers(currentWeathers);
		sample = sampleDao.save(sample);

		log.info("Persisted {} states: {} new, {} unchanged from previous. Delta vs previous sample: {}",
				totalCurrentWeathersCount,
				totalCurrentWeathersCount - unchangedWeathersCount,
				unchangedWeathersCount,
				totalCurrentWeathersCount - totalPreviousStatesCount);

		return sample;
	}

	private void buildLocalEntityContext() {
		previousStates = fetchPreviousWeathers();
		cityDao.initTransaction();
		weatherConditionDao.initTransaction();
	}

	private Map<City, Weather> fetchPreviousWeathers() {
		log.info("Fetching previous BikeState entities from database...");
		Set<Weather> previous = sampleDao.getPrevious()
				.map(Sample::getWeathers)
				.orElse(new HashSet<>());
		log.info("Found {} most recent BikeState entities in database", previous.size());

		return previous.stream()
				.collect(Collectors.toMap(Weather::getCity, weather -> weather));
	}


	private Weather choseCurrentWeather(Weather previous, Weather notSynced) {
		return notSynced.equals(previous) ? previous :  setWeatherForSyncing(notSynced);
	}

	private Weather setWeatherForSyncing(Weather notSynced) {
		notSynced.setCity(cityDao.sync(notSynced.getCity()));
		notSynced.setCondition(weatherConditionDao.sync(notSynced.getCondition()));
		previousStates.put(notSynced.getCity(), notSynced);

		return notSynced;
	}

	@Transactional
	public Weather create(OwmCityDto dto, City city, Sample sample) {
		Weather newWeather = new Weather();

		newWeather.setClouds(dto.getClouds());
		newWeather.setHumidity(dto.getHumidity());
		newWeather.setHumanTemp(dto.getHuman_temp());
		newWeather.setTemp(dto.getTemp());
		newWeather.setPressure(dto.getPressure());
		newWeather.setRain(dto.getRain());
		newWeather.setSnow(dto.getSnow());
		newWeather.setPressure(dto.getPressure());
		newWeather.setWindDeg(dto.getWind_deg());
		newWeather.setWindSpeed(dto.getWind_speed());

		newWeather.setCondition(createWeatherCondition(dto.getCondition_id(), dto.getDescription()));

		newWeather.setCity(city);

		newWeather.setCalculatedTime(dto.getCalculatedTime());
		newWeather.setSunrise(dto.getSunrise());
		newWeather.setSunset(dto.getSunset());



		return weatherRepository.save(newWeather);
	}

	@Transactional
	public Map<City, Weather> getAllByCalculatedTime(Instant calculatedTime) {

		Map<City, Weather> weathers = new HashMap<>();
		weatherRepository.findAllByCalculatedTime(calculatedTime)
				.forEach(weather -> weathers.put(weather.getCity(), weather));

		return weathers;
	}

	@Transactional
	public Optional<Weather> getLastWeatherForCity(City city) {
		return weatherRepository.findFirstByCityOrderByCalculatedTimeDesc(city);
	}

	private WeatherCondition createWeatherCondition(Long apiId, String description) {
		WeatherCondition weatherCondition = weatherConditionRepository.findByApiId(apiId)
				.orElseGet(() -> {
					WeatherCondition condition = new WeatherCondition(apiId, description);
					return weatherConditionRepository.save(condition);
				});

		return weatherCondition;
	}


}
