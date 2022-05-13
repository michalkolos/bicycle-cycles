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


	@Autowired
	public WeatherDao(WeatherRepository weatherRepository,
	                  WeatherConditionRepository weatherConditionRepository,
	                  CityDao cityDao,
	                  SampleDao sampleDao) {

		this.weatherRepository = weatherRepository;
		this.weatherConditionRepository = weatherConditionRepository;
		this.cityDao = cityDao;
		this.sampleDao = sampleDao;
	}

	private Map<City, Weather> previousStates = new ConcurrentHashMap<>();


	public Sample persistSample(Sample sample) {
		log.info("Starting persisting new weather data...");

		cityDao.initTransaction();

	}

	private void uploadPreviousState() {
		log.info("Fetching previous BikeState entities from database...");
		Set<BikeState> previous = sampleDao.getPrevious()
				.map(Sample::getBikeStates)
				.orElse(new HashSet<>());

		previousStates = generateStateMap(previous);

		log.info("Found {} most recent BikeState entities in database", previous.size());
	}

	private Map<City, Weather> generateStateMap(Set<BikeState> stateSet) {
		return  stateSet.stream()
				.collect(Collectors.toMap(BikeState::getBike, state -> state));
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
