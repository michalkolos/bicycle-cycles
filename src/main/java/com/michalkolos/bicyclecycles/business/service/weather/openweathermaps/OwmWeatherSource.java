/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalkolos.bicyclecycles.business.service.weather.WeatherSource;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDtoToWeatherConverter;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmGroupCityDto;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.utils.DownloaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Component
@Slf4j
public class OwmWeatherSource extends OwmCommon implements WeatherSource {

	public static final int GROUP_DOWNLOAD_COUNT = 20;

	@Autowired
	public OwmWeatherSource(ObjectMapper objectMapper, DownloaderService downloaderService) {
		super(objectMapper, downloaderService);
	}

	@Override
	public Set<Weather> get(Collection<City> cities) {
		Set<City> viableCities = cities.stream()
				.map(this::fetchOwmApiId)
				.filter(city -> city.getOwmId() != null)
				.collect(Collectors.toSet());

		Set<Weather> weathers = packageCities(viableCities).stream()
				.flatMap(cityPackage -> downloadCityPackage(cityPackage).stream())
				.collect(Collectors.toSet());

		log.info("Downloaded {} weather data out of {} cities.", weathers.size(), cities.size());

		return weathers;
	}

	private Set<Set<City>> packageCities(Collection<City> cities) {
		Set<Set<City>> cityPacks = new HashSet<>();
		int counter = GROUP_DOWNLOAD_COUNT;
		Set<City> pack = new HashSet<>();

		for(City city : cities) {
			if(counter >= GROUP_DOWNLOAD_COUNT) {
				counter = 0;
				pack = new HashSet<>();
				cityPacks.add(pack);
			}
			pack.add(city);

			counter++;
		}

		return cityPacks;
	}

	private Set<Weather> downloadCityPackage(Set<City> cities) {

		return Optional.of(buildUrlStringFromCities(cities))
				.flatMap(this::downloadData)
				.map(this::parseGroupResponse)
				.map(dtos -> convertGroupOfDtoToEntities(dtos, cities))
				.orElse(new HashSet<>());
	}

	private String buildUrlStringFromCities(Set<City> cities) {
		String idString = cities.stream()
				.flatMap(city -> resolveOwmIdForCity(city).stream())
				.collect(joining(","));

		return OWM_API_URI + "group?" + OWM_API_KEY + "&" + OWM_API_UNITS +
				"&id=" + idString;
	}

	private Optional<String> resolveOwmIdForCity(City city) {
		return Optional.of(city)
				.map(City::getOwmId)
				.map(String::valueOf);
	}

	private Optional<String> downloadData(String urlString) {
		waitForApi();
		return downloaderService.fromUrl(urlString, DOWNLOAD_RETRIES);
	}

	private Set<OwmCityDto> parseGroupResponse(String responseBody) {
		try {
			return objectMapper.readValue(responseBody, OwmGroupCityDto.class).getList();
		} catch (JsonProcessingException e) {
			log.warn("Error parsing group Openweathermaps weather data, " + e.getMessage());
		}
		return null;
	}

	private Set<Weather> convertGroupOfDtoToEntities(Set<OwmCityDto> dtos, Set<City> cities) {
		Map<Long, City> cityMap = cities.stream()
				.collect(Collectors.toMap(City::getOwmId, city -> city));

		return dtos.stream()
				.flatMap(dto -> entityFromDtoInGroup(dto, cityMap).stream())
				.collect(Collectors.toSet());
	}

	private Optional<Weather> entityFromDtoInGroup(OwmCityDto dto, Map<Long, City> cityMap) {
		return Optional.ofNullable(cityMap.get(dto.getId()))
				.map(city -> OwmCityDtoToWeatherConverter.convert(dto, city));
	}
}
