/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.business.service.weather.WeatherSource;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDtoToWeatherConverter;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmGroupCityDto;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Component
public class OwmWeatherSource implements WeatherSource {
	public static final String OWM_API_URI = "https://api.openweathermap.org/data/2.5/";
	public static final String OWM_API_KEY = "appid=216353d6d8f732836dc5f5cd45404903";
	public static final String OWM_API_UNITS = "units=metric";
	public static final int GROUP_DOWNLOAD_COUNT = 20;
	public static final int DELAY_SEC = 1;

	private Instant operationTimer = Instant.now();

  	private static final Logger logger = LoggerFactory.getLogger(OwmWeatherSource.class);
	@Qualifier("objectMapper")
	private final ObjectMapper objectMapper;

	@Autowired
	public OwmWeatherSource(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}



//         :::::::: ::::::::::: ::::    :::  ::::::::  :::        ::::::::::
//       :+:    :+:    :+:     :+:+:   :+: :+:    :+: :+:        :+:
//      +:+           +:+     :+:+:+  +:+ +:+        +:+        +:+
//     +#++:++#++    +#+     +#+ +:+ +#+ :#:        +#+        +#++:++#
//           +#+    +#+     +#+  +#+#+# +#+   +#+# +#+        +#+
//   #+#    #+#    #+#     #+#   #+#+# #+#    #+# #+#        #+#
//   ######## ########### ###    ####  ########  ########## ##########

	public Optional<Weather> getPointWeather(final City city) {
		waitForApi();
		return Optional.ofNullable(city)
				.map(City::getBounds)
				.map(Geometry::getCentroid)
				.map(this::buildPointRequest)
				.map(this::sendRequest)
				.map(this::parsePointResponse)
				.map((OwmCityDto dto) -> updateApiId(dto, city))
				.map(dto -> OwmCityDtoToWeatherConverter.convert(dto, city));
	}

//	::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	private HttpRequest buildPointRequest(Point point) {
		return HttpRequest.newBuilder()
				.uri(URI.create(OWM_API_URI + "weather?" + OWM_API_KEY + "&" + OWM_API_UNITS +
						"&lat=" + point.getCoordinate().getY() +
						"&lon=" + point.getCoordinate().getX()))
				.GET()
				.build();
	}

	private OwmCityDto parsePointResponse(String responseBody) {
		try {
			return objectMapper.readValue(responseBody, OwmCityDto.class);
		} catch (JsonProcessingException e) {
			logger.warn("Error parsing point Openweathermaps weather data, " + e.getMessage());
		}
		return null;
	}

	private OwmCityDto updateApiId(OwmCityDto dto, City city) {
		if(city.getOwmId() == null) {
			city.setOwmId(dto.getId());
			logger.info(String.format("Set Openweathermaps ID for city %s (%s) to %d",
					city.getName(),
					city.getAlias(),
					city.getOwmId()));

		} else if(!city.getOwmId().equals(dto.getId())) {
			logger.warn(String.format("Openweathermaps ID for city %s (%s) has changed from %d to %d",
					city.getName(),
					city.getAlias(),
					city.getOwmId(),
					dto.getId()));
			city.setOwmId(dto.getId());
		}
		city.setOwmId(dto.getId());
		return dto;
	}



//              ::::::::  :::::::::   ::::::::  :::    ::: :::::::::
//            :+:    :+: :+:    :+: :+:    :+: :+:    :+: :+:    :+:
//           +:+        +:+    +:+ +:+    +:+ +:+    +:+ +:+    +:+
//          :#:        +#++:++#:  +#+    +:+ +#+    +:+ +#++:++#+
//         +#+   +#+# +#+    +#+ +#+    +#+ +#+    +#+ +#+
//        #+#    #+# #+#    #+# #+#    #+# #+#    #+# #+#
//        ########  ###    ###  ########   ########  ###

	public Set<Weather> getGroupWeather(Collection<City> cities) {
		Set<Weather> weathers = new HashSet<>();
		Set<City> readyForGrouping = new HashSet<>();

		cities.forEach(city -> {
			Optional.ofNullable(city.getOwmId()).ifPresentOrElse(
							id -> readyForGrouping.add(city),
							() -> getPointWeather(city).ifPresent(weathers::add));
		});

		packageCities(readyForGrouping).stream()
				.map(this::downloadGroup)
				.forEach(weathers::addAll);

		logger.info("Downloaded weather data for " + weathers.size() + " cities.");

		return weathers;
	}

//	::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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

	private Set<Weather> downloadGroup(Collection<City> cities) {
		Set<Long> ids = cities.stream()
				.map(City::getOwmId)
				.collect(Collectors.toSet());

		HttpRequest request = buildGroupRequest(ids);

		waitForApi();
		return Optional.ofNullable(sendRequest(request))
				.map(this::parseGroupResponse)
				.map(dtos -> deserializeGroup(dtos, cities))
				.orElse(new HashSet<>());
	}

	private HttpRequest buildGroupRequest(Collection<Long> ids) {
		return HttpRequest.newBuilder()
				.uri(URI.create(OWM_API_URI + "group?" + OWM_API_KEY + "&" + OWM_API_UNITS +
						"&id=" + ids.stream().map(String::valueOf).collect(joining(","))))
				.GET()
				.build();
	}

	private Set<OwmCityDto> parseGroupResponse(String responseBody) {
		try {
			return objectMapper.readValue(responseBody, OwmGroupCityDto.class).getList();
		} catch (JsonProcessingException e) {
			logger.warn("Error parsing group Openweathermaps weather data, " + e.getMessage());
		}
		return null;
	}
	private Set<Weather> deserializeGroup(Collection<OwmCityDto> dtos, Collection<City> cities) {
		Map<Long, City> cityMap = new HashMap<>();
		cities.forEach(city -> cityMap.put(city.getOwmId(), city));

		return dtos.stream()
				.map(dto -> OwmCityDtoToWeatherConverter.convert(dto, cityMap.get(dto.getId())))
				.collect(Collectors.toSet());
	}





//      ::::::::   ::::::::    :::   :::     :::   :::    ::::::::  ::::    :::
//    :+:    :+: :+:    :+:  :+:+: :+:+:   :+:+: :+:+:  :+:    :+: :+:+:   :+:
//   +:+        +:+    +:+ +:+ +:+:+ +:+ +:+ +:+:+ +:+ +:+    +:+ :+:+:+  +:+
//  +#+        +#+    +:+ +#+  +:+  +#+ +#+  +:+  +#+ +#+    +:+ +#+ +:+ +#+
// +#+        +#+    +#+ +#+       +#+ +#+       +#+ +#+    +#+ +#+  +#+#+#
//#+#    #+# #+#    #+# #+#       #+# #+#       #+# #+#    #+# #+#   #+#+#
//########   ########  ###       ### ###       ###  ########  ###    ####


	//  TODO: make critical section for multithreading.
	private void waitForApi() {
		while(operationTimer.plusSeconds(DELAY_SEC).isAfter(Instant.now())) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		operationTimer = Instant.now();
	}

	private String sendRequest(HttpRequest request) {
		try {

			HttpClient client = HttpClient.newBuilder().build();
			return client.send(
					request, HttpResponse.BodyHandlers.ofString()).body();

		} catch (UncheckedIOException | IOException | InterruptedException e) {
			logger.warn("Error sending HTTP request to Openweathermaps service, " + e.toString());
		}

		return null;
	}
}
