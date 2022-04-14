/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.openweathermaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.dto.OwmCityDto;
import com.michalkolos.bicyclecycles.business.service.openweathermaps.dto.OwmGroupCityDto;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Service
public class OwmDownloadService {

	public static final String OWM_API_URI = "https://api.openweathermap.org/data/2.5/";
	public static final String OWM_API_KEY = "appid=216353d6d8f732836dc5f5cd45404903";
	public static final String OWM_API_UNITS = "units=metric";

	public static final int GROUP_DOWNLOAD_COUNT = 20;

	private static final Logger logger = LoggerFactory.getLogger(OwmDownloadService.class);

	@Qualifier("objectMapper")
	private final ObjectMapper objectMapper;

	public static final int DELAY_SEC = 1;
	private Instant operationTimer = Instant.now();


	@Autowired
	public OwmDownloadService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}



	public Optional<OwmCityDto> getSingleWeather(Point coords) {
		delay();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(OWM_API_URI + "weather?" + OWM_API_KEY + "&" + OWM_API_UNITS +
						"&lat=" + coords.getCoordinate().getY() +
						"&lon=" + coords.getCoordinate().getX()))
				.GET()
				.build();

		return Optional.ofNullable(sendRequest(request))
				.map(this::parseResponse);
	}

	public Set<OwmCityDto> getGroupWeather(Set<Long> ids) {
		Set<OwmCityDto> dtos = new HashSet<>();

		packageIds(ids).forEach(pack -> {
			dtos.addAll(downloadGroup(pack));
		});

		logger.info("Downloaded weather data for " + dtos.size() + " cities.");
		return dtos;
	}

	private Set<Set<Long>> packageIds(Set<Long> ids) {
		Set<Set<Long>> idPacks = new HashSet<>();
		Set<Long> pack = new HashSet<>();
		int counter = GROUP_DOWNLOAD_COUNT;

		for(Long id : ids) {
			if(counter >= GROUP_DOWNLOAD_COUNT) {
				counter = 0;
				pack = new HashSet<>();
				idPacks.add(pack);
			}
			pack.add(id);

			counter++;
		}

		return idPacks;
	}

	private Set<OwmCityDto> downloadGroup(Set<Long> ids) {
		delay();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(OWM_API_URI + "group?" + OWM_API_KEY + "&" + OWM_API_UNITS +
						"&id=" + ids.stream().map(String::valueOf).collect(joining(","))))
				.GET()
				.build();

		return Optional.ofNullable(sendRequest(request))
				.map(this::parseGroupResponse)
				.orElse(new HashSet<>());
	}



	//  TODO: make critical section for multithreading.
	private void delay() {
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
			logger.warn("Error sending HTTP request, " + e.toString());
		}

		return null;
	}


	private OwmCityDto parseResponse(String responseBody) {
		try {

			return objectMapper.readValue(responseBody, OwmCityDto.class);

		} catch (JsonProcessingException e) {
			logger.warn("Error parsing response message, " + e.getMessage());
		}
		return null;
	}

	private Set<OwmCityDto> parseGroupResponse(String responseBody) {
		try {

			return objectMapper.readValue(responseBody, OwmGroupCityDto.class).getList();

		} catch (JsonProcessingException e) {
			logger.warn("Error parsing response message, " + e.getMessage());
		}
		return null;
	}
}
