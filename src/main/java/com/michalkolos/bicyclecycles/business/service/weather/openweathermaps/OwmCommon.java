/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.utils.DownloaderService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.joining;

@Slf4j
public abstract class OwmCommon {
	public static final String OWM_API_URI = "https://api.openweathermap.org/data/2.5/";
	public static final String OWM_API_KEY = "appid=216353d6d8f732836dc5f5cd45404903";
	public static final String OWM_API_UNITS = "units=metric";
	public static final int DOWNLOAD_RETRIES = 20;
	public static final int DELAY_SEC = 1;



	private Instant operationTimer = Instant.now();

	@Qualifier("objectMapper")
	protected final ObjectMapper objectMapper;
	protected final DownloaderService downloaderService;

	@Autowired
	public OwmCommon(ObjectMapper objectMapper, DownloaderService downloaderService) {
		this.objectMapper = objectMapper;
		this.downloaderService = downloaderService;
	}

	//  TODO: make critical section for multithreading.
	protected void waitForApi() {
		while(operationTimer.plusSeconds(DELAY_SEC).isAfter(Instant.now())) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		operationTimer = Instant.now();
	}

	public City checkOwmApiId(final City city) {

		Optional.of(city)
				.map(City::getOwmId)
				.filter(id -> id != 0)
				.ifPresentOrElse(
						id -> log.debug("City {} has OWM API ID: {}",
								city.getName(),
								city.getOwmId()),
						() -> downloadOwmApiId(city)
				);



		return city;
	}

	public void downloadOwmApiId(final City city) {
		waitForApi();

		Optional.of(city)
				.map(City::getBounds)
				.map(Geometry::getCentroid)
				.map(this::buildRequestUrl)
				.flatMap(this::sendRequest)
				.map(this::parsePointResponse)
				.ifPresentOrElse(
						dto -> {
							city.setOwmId(dto.getId());
							log.info("Set Openweathermaps ID for city {}({}) to {}.",
									city.getName(), city.getAlias(), city.getOwmId());
						},
						() -> log.warn("Unable to get Openweather API ID for city {}({}).",
								city.getName(), city.getAlias()));
	}

	private String buildRequestUrl(Point point) {
		return (OWM_API_URI + "weather?" + OWM_API_KEY + "&" + OWM_API_UNITS +
				"&lat=" + point.getCoordinate().getY() +
				"&lon=" + point.getCoordinate().getX());
	}

	private Optional<String> sendRequest(String request) {
		waitForApi();
		return downloaderService.fromUrl(request, DOWNLOAD_RETRIES);
	}

	private OwmCityDto parsePointResponse(String responseBody) {
		try {
			return objectMapper.readValue(responseBody, OwmCityDto.class);
		} catch (JsonProcessingException e) {
			log.warn("Error parsing point Openweathermaps weather data, " + e.getMessage());
		}
		return null;
	}
}
